package com.kshrd.krorya.model.enumeration;

public enum CookingTimeEnum {
    UNDER_15_MINUTES("Under 15 minutes"),
    UNDER_30_MINUTES("Under 30 minutes"),
    UNDER_60_MINUTES("Under 60 minutes");

    private final String label;

    CookingTimeEnum(String label) {
        this.label = label;
    }


}
