package com.gtr3base.AvByAnalog.enums;

public enum CarStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static CarStatus fromString(String status) {
        if(status == null){
            return PENDING;
        }
        try {
            return CarStatus.valueOf(status.toUpperCase());
        }catch (IllegalArgumentException e){
            return PENDING;
        }
    }

    public boolean isPending(){
        return this == PENDING;
    }

    public boolean isApproved(){
        return this == APPROVED;
    }

    public boolean isRejected(){
        return this == REJECTED;
    }

    public boolean canBeModified() {
        return this == PENDING;
    }

    public boolean canBeDeleted() {
        return this == PENDING;
    }

    public String getDisplayName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
    public boolean canTransitionTo(CarStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == APPROVED || newStatus == REJECTED;
            case APPROVED -> newStatus == PENDING || newStatus == REJECTED;
            case REJECTED -> newStatus == PENDING || newStatus == APPROVED;
        };
    }

    public CarStatus[] getAvailableTransitions() {
        return switch (this) {
            case PENDING -> new CarStatus[]{APPROVED, REJECTED};
            case APPROVED -> new CarStatus[]{PENDING, REJECTED};
            case REJECTED -> new CarStatus[]{PENDING, APPROVED};
        };
    }
}
