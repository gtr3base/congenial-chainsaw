package com.gtr3base.AvByAnalog.enums;

public enum UserRole {
    USER,
    ADMIN;

    public static UserRole fromString(String role) {
        if(role == null){
            return USER;
        }
        try{
            return UserRole.valueOf(role.toUpperCase());
        }catch (IllegalArgumentException e){
            return USER;
        }
    }

    public boolean isAdmin(){
        return this == ADMIN;
    }

    public boolean isUser(){
        return this == USER;
    }

    public String getDisplayName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
