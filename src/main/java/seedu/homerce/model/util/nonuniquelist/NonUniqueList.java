package seedu.homerce.model.util.nonuniquelist;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.homerce.model.expense.exceptions.ItemNotFoundException;

public class NonUniqueList<T> implements Iterable<T> {

    private final ObservableList<T> internalList = FXCollections.observableArrayList();
    private final ObservableList<T> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Adds an item to the list.
     */
    public void add(T toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Replaces the item {@code target} in the list with {@code editedExpense} or {@code editedRevenue}.
     * {@code target} must exist in the list.
     */
    public void setItem(T target, T editedItem) {
        requireAllNonNull(target, editedItem);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ItemNotFoundException();
        }

        internalList.set(index, editedItem);
    }

    /**
     * Removes the equivalent item from the list.
     * The item must exist in the list.
     */
    public void remove(T toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ItemNotFoundException();
        }
    }

    public void setItems(NonUniqueList<T> replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code expenses}.
     */
    public void setItems(List<T> items) {
        requireAllNonNull(items);
        internalList.setAll(items);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<T> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<T> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NonUniqueList // instanceof handles nulls
                && internalList.equals(((NonUniqueList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    public Stream<T> stream() {
        return internalList.stream();
    }

    public int size() {
        return internalList.size();
    }

    /**
     * Creates a deep copy of all the non unique list of items.
     *
     * @return a list of deep copied items.
     */
    public List<T> deepCopy() {
        Iterator<T> iterator = this.iterator();
        List<T> itemsCopy = new ArrayList<>();
        while (iterator.hasNext()) {
            itemsCopy.add(iterator.next());
        }
        return itemsCopy;
    }
}


