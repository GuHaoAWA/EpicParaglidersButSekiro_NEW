package com.guhao.sekiro.capabilities;

public interface PlayerMovementInterface {




    /**
     * TODO: Maybe test separating these into their own separate interfaces
     *       if this becomes too large?
     * ServerPlayerMovement
     */
    void setTotalActionStaminaCostServerSide(int totalActionStaminaCost);
    void setActionStaminaCostServerSide(int attackStaminaCost);
    void attackingServerSide(boolean isAttacking);
    boolean isAttackingServerSide();
    void performingActionServerSide(boolean isPerformingAction);
    boolean isPerformingActionServerSide();

    /**
     * PlayerMovement
     */
    int getTotalActionStaminaCost();
    void setTotalActionStaminaCost(int totalActionStaminaCost);

    /**
     * ClientPlayerMovement
     */
    void setTotalActionStaminaCostClientSide(int totalActionStaminaCost);


}



