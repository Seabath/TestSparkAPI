package com.seabath.common;

import lombok.Getter;

/**
 * Status of tests and test suite.
 */
public enum Status {
    SUCCESS(0) {
        @Override
        public Status changeSuccess() {
            return SUCCESS;
        }
    },
    FLAKY(1) {
        @Override
        public Status changeSuccess() {
            return FLAKY;
        }
    },
    FAIL(2) {
        @Override
        public Status changeSuccess() {
            return FLAKY;
        }
    },
    IN_PROGRESS(3) {
        @Override
        public Status changeSuccess() {
            return SUCCESS;
        }
    },
    INTERRUPTED(4) {
        @Override
        public Status changeSuccess() {
            return INTERRUPTED;
        }
    };

    /**
     * Field used to compare status one with another.
     */
    @Getter
    private final int value;

    Status(int value) {
        this.value = value;
    }

    /**
     * Give new status for success state.
     *
     * @return New status
     */
    public abstract Status changeSuccess();
}
