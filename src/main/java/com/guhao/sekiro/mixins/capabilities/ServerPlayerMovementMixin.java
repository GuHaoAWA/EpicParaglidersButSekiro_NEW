package com.guhao.sekiro.mixins.capabilities;
import com.guhao.sekiro.EpicParaglidersButSekiroMod;
import com.guhao.sekiro.capabilities.PlayerMovementInterface;
import com.guhao.sekiro.network.ModNet;
import com.guhao.sekiro.network.SyncActionToClientMsg;
import com.guhao.sekiro.utils.MathUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.PlayerState;
import tictim.paraglider.capabilities.ServerPlayerMovement;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = ServerPlayerMovement.class , remap = false)
public abstract class ServerPlayerMovementMixin extends PlayerMovement implements PlayerMovementInterface {
    @Shadow @Final private ServerPlayer serverPlayer;
    private int totalActionStaminaCost;
    private int currentActionStaminaCost;
    private boolean isAttacking;
    private boolean isPerformingAction;

    public ServerPlayerMovementMixin(Player player) { super(player); }

    @Override
    public void performingActionServerSide(boolean isPerformingAction) {
        this.isPerformingAction = isPerformingAction;
    }

    @Override
    public void isAttackingServerSide(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    @Override
    public void setTotalActionStaminaCostServerSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }

    @Override
    public void setActionStaminaCostServerSide(int actionStaminaCost) {
        this.currentActionStaminaCost = actionStaminaCost;
    }

    /**
     * Updates the server side player by doing a few things. If the player is attacking, then the amount of
     * stamina to drain will be calculated. If there is an overlap between a skill being used (e.g., rolling)
     * and an attack (e.g., rolling immediately after an attack to cancel the animation), then that will be
     * accounted for in the total stamina cost so that there isn't any uneven amount of stamina drained.
     */
    @Inject(method = "update", at = @At(value = "HEAD"),  remap=false)
    public void update(CallbackInfo ci) {
        ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);

        /**
         * TODO: Put into own method when the above TODO for the ServerPlayerPatch is done.
         *
         * Will disable Epic Fight's battle mode if the player is paragliding.
         * This fixes the awkward issue where the player can still attack while gliding.
         */
        if (this.isParagliding() && serverPlayerPatch.isBattleMode()) {
            serverPlayerPatch.toggleMode();
        }

        //TODO: Would like to organize these better.
//        checkShieldDisable();
        calculateRangeStaminaCost();

        if (isAttacking && serverPlayerPatch.getEntityState().attacking()) {
            this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(this.totalActionStaminaCost)
                    + MathUtils.calculateTriangularNumber(currentActionStaminaCost)));
            isAttacking = false;
            isPerformingAction = true;
        }
        else if (isPerformingAction) {
            this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(this.totalActionStaminaCost)
                    + MathUtils.calculateTriangularNumber(currentActionStaminaCost)));
        }

        //TODO: Is this necessary?
        if(!this.player.isCreative() && this.isDepleted()){
            this.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }

        if(this.isPerformingAction) {
            ModNet.NET.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncActionToClientMsg(this.totalActionStaminaCost));
            this.currentActionStaminaCost = 0;
            this.isPerformingAction = false;
        }

        this.setTotalActionStaminaCost(this.totalActionStaminaCost);
    }

    /**
     * Calculates ranged attacks from bows and crossbows. Currently, this just uses a base amount of 6
     * to drain constantly while the use button is held down. Will flesh this out in a later version.
     */
    private void calculateRangeStaminaCost() {
        //TODO: Flesh out the math for this, then add it to the MathUtils class.
        //      Also, this will need attribute support as well.
        //      Can probably even check if the weapon is being pulled back or not using 'projectileWeaponItem'
        if (player.getUseItem().getItem() instanceof  ProjectileWeaponItem projectileWeaponItem) {
            this.currentActionStaminaCost = 6;
            this.isPerformingAction = true;
        }
    }

    /**
     * Checks if the player is currently holding a shield item. If so, then the modifyShieldCooldown method is called
     * to determine what to do with the shield.
     */
    private void checkShieldDisable() {
        if (player.getOffhandItem().getItem() instanceof ShieldItem offhandShieldItem) {
            modifyShieldCooldown(offhandShieldItem);
        }
        else if (player.getMainHandItem().getItem() instanceof ShieldItem mainHandShieldItem) {
            modifyShieldCooldown(mainHandShieldItem);
        }
    }

    /**
     * Disables the shield cooldown UNLESS the player's current stamina is fully depleted. If the stamina is depleted,
     * then the cooldown is set to the amount of ticks remaining until the player's stamina is fully replenished. Has
     * some additional checks to ensure the shield cooldown time stays in sync with the stamina replenish time as well.
     *
     * @param shieldItem A main hand or offhand shield being held by the player
     */
    private void modifyShieldCooldown(ShieldItem shieldItem) {
        if (player.getCooldowns().isOnCooldown(shieldItem) && !this.isDepleted()) {
            EpicParaglidersButSekiroMod.LOGGER.info("REMOVING COOLDOWN");
            player.getCooldowns().removeCooldown(shieldItem);
        }
        else if (this.isDepleted()) {
            int recoveryRate = PlayerState.IDLE.change();
            int currentRecoveredAmount = this.getStamina();
            float cooldownPercentage = player.getCooldowns().getCooldownPercent(shieldItem, 0.0F);
            int shieldRecoveryDelay = (int) (this.getMaxStamina() * (1 - cooldownPercentage));
            if (shieldRecoveryDelay > currentRecoveredAmount) {
                player.getCooldowns().addCooldown(shieldItem, (this.getMaxStamina() - currentRecoveredAmount) / recoveryRate);
            }
        }
    }
}