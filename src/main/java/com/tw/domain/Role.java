package com.tw.domain;

public enum Role {
    EMPLOYEE, PROJECT_MANAGER, BACKGROUND_JOB, ADMIN;

    public boolean equalsWithObject(Object object){
        return this.equals(Role.valueOf(String.valueOf(object)));
    }
}
