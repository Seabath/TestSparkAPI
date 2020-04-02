package common;

import lombok.Getter;

/**
 * Status of tests and test suite.
 */
public enum Status {
    SUCCESS(0),
    FLAKY(1),
    FAIL(2),
    IN_PROGRESS(3),
    INTERRUPTED(4);

    /**
     * Field used to compare status one with another.
     */
    @Getter
    private int value;

    Status(int value) {
        this.value = value;
    }
}
