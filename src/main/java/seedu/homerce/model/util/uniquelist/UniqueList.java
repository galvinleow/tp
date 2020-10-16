package seedu.homerce.model.util.uniquelist;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.homerce.model.util.uniquelist.exceptions.DuplicateItemException;
import seedu.homerce.model.util.uniquelist.exceptions.ItemNotFoundException;

/**
 * A list that enforces uniqueness between its elements and does not allow nulls.
 * A client is considered unique by comparing using {@code T#isSame(T)}. As such, adding and updating of items in the
 * list uses T#isSame(T) for equality so as to ensure that the item being added or updated is
 * unique in terms of identity in the UniqueList. However, the removal of an item uses T#equals(Object) so
 * as to ensure that the client with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 */
public class UniqueList<T extends UniqueListItem> implements Iterable<T> {

    protected final ObservableList<T> internalList = FXCollections.observableArrayList();
    protected final ObservableList<T> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent item as the given argument.
     */
    public boolean contains(T toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSame);
    }

    /**
     * Adds an item to the list.
     * The item must not already exist in the list.
     */
    public void add(T toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateItemException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the item {@code target} in the list with {@code edited}.
     * {@code target} must exist in the list.
     * The item identity of {@code edited} must not be the same as another existing item in the list.
     */
    public void setItem(T target, T edited) {
        requireAllNonNull(target, edited);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ItemNotFoundException();
        }

        if (!target.isSame(edited) && contains(edited)) {
            throw new DuplicateItemException();
        }

        internalList.set(index, edited);
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

    public void setItems(UniqueList<T> replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code items}.
     * {@code items} must not contain duplicate items.
     */
    public void setItems(List<T> items) {
        requireAllNonNull(items);
        if (!itemsAreUnique(items)) {
            throw new DuplicateItemException();
        }

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
                || (other instanceof UniqueList // instanceof handles nulls
                && internalList.equals(((UniqueList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code items} contains only unique items.
     */
    private boolean itemsAreUnique(List<T> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                if (items.get(i).isSame(items.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets number of items in the UniqueList.
     *
     * @return size of the UniqueList.
     */
    public int size() {
        return internalList.size();
    }

    /**
     * Gets a stream of all the items in the UniqueList.
     *
     * @return stream of all items in the UniqueList.
     */
    public Stream<T> stream() {
        return internalList.stream();
    }

    /**
     * Creates a deep copy of all the items in the unique list.
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
