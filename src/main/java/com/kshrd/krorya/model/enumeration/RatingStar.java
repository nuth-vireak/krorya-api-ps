package com.kshrd.krorya.model.enumeration;

public enum RatingStar {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        private final int value;

        RatingStar(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

}
