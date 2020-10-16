package seedu.homerce.model.manager;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.homerce.model.revenue.Revenue;
import seedu.homerce.model.util.nonuniquelist.NonUniqueList;

/**
 * Wraps all data at the RevenueTracker level
 */
public class RevenueTracker implements ReadOnlyRevenueTracker {

    private final NonUniqueList<Revenue> revenues;

    public RevenueTracker() {
        this.revenues = new NonUniqueList<>();
    }

    /**
     * Creates an RevenueTracker using the Revenue in the {@code toBeCopied}
     */
    public RevenueTracker(ReadOnlyRevenueTracker toBeCopied) {
        this.revenues = new NonUniqueList<>();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Resets the existing data of this {@code ServiceManager} with {@code newData}.
     */
    public void resetData(ReadOnlyRevenueTracker newData) {
        requireNonNull(newData);
        setRevenues(newData.getRevenueList());
    }

    /**
     * Replaces the contents of the revenue list with {@code revenues}.
     * {@code services} can contain duplicate revenues.
     */
    public void setRevenues(List<Revenue> revenues) {
        this.revenues.setItems(revenues);
    }

    //// service-level operations

    /**
     * Replaces the given revenue {@code target} in the list with {@code editedRevenue}.
     * {@code target} must exist in the SuperSalon.
     * The service identity of {@code editedService} must not be the same as another existing service in the SuperSalon.
     */
    public void setRevenues(Revenue target, Revenue editedRevenue) {
        requireNonNull(editedRevenue);
        revenues.setItem(target, editedRevenue);
    }

    /**
     * Adds a revenues to the SuperSalon.
     */
    public void addRevenue(Revenue r) {
        revenues.add(r);
    }


    /**
     * Removes {@code key} from this {@code ServiceManager}.
     * {@code key} must exist in the SuperSalon.
     */
    public void removeRevenue(Revenue key) {
        revenues.remove(key);
    }

    public List<Revenue> filterByMonth(Predicate<Revenue> predicate) {
        return revenues.stream().filter(x -> predicate.test(x)).collect(Collectors.toList());
    }
    public List<Revenue> filterByYear(Predicate<Revenue> predicate) {
        return revenues.stream().filter(x -> predicate.test(x)).collect(Collectors.toList());
    }

    //// util methods

    @Override
    public String toString() {
        return "Service Manager:\n"
                + revenues.stream().map(Revenue::toString).collect(Collectors.joining("\n"))
                + "\n Total number of activities: " + revenues.size();

    }

    @Override
    public ObservableList<Revenue> getRevenueList() {
        return revenues.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RevenueTracker // instanceof handles nulls
                && revenues.equals(((RevenueTracker) other).revenues));
    }

    @Override
    public int hashCode() {
        return Objects.hash(revenues);
    }

    /**
     * Creates a deep copy of all the revenues in the list of revenues.
     *
     * @return a list of revenues.
     */
    public RevenueTracker deepCopy() {
        List<Revenue> internalListCopy = revenues.deepCopy();
        RevenueTracker revenueTrackerCopy = new RevenueTracker();
        revenueTrackerCopy.setRevenues(internalListCopy);
        return revenueTrackerCopy;
    }
}
