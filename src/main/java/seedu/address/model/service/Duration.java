package seedu.address.model.service;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.AppUtil;

public class Duration {
    public static final String MESSAGE_CONSTRAINTS = "Duration (in half hours intervals - 0.5) must be a valid double "
            + "more than 0.5 and at most 24.0";

    private static final double MAX_VALUE = 24.0;
    private static final double MIN_VALUE = 0.5;
    public static final Predicate<Double> VALIDATION_PREDICATE = i -> i >= MIN_VALUE && i <= MAX_VALUE && i % 0.5 == 0;


    public final Double value;

    public Duration(Double duration) {
        requireNonNull(duration);
        AppUtil.checkArgument(isValidDuration(duration), MESSAGE_CONSTRAINTS);
        value = duration;
    }

    /**
     * Returns true if a given string is a valid duration.
     */
    public static boolean isValidDuration(Double test) {
        return VALIDATION_PREDICATE.test(test);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Duration // instanceof handles nulls
                && value.equals(((Duration) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
