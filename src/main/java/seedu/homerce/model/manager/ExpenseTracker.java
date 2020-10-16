package seedu.homerce.model.manager;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.util.nonuniquelist.NonUniqueList;

public class ExpenseTracker implements ReadOnlyExpenseTracker {
    private final NonUniqueList<Expense> expenses;

    public ExpenseTracker() {
        this.expenses = new NonUniqueList<>();
    }

    /**
     * Creates an ExpenseTracker using the Expenses in the {@code toBeCopied}
     */
    public ExpenseTracker(ReadOnlyExpenseTracker toBeCopied) {
        this.expenses = new NonUniqueList<>();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setItems(expenses);
    }

    /**
     * Resets the existing data of this {@code ExpenseTracker} with {@code newData}.
     */
    public void resetData(ReadOnlyExpenseTracker newData) {
        requireNonNull(newData);
        setExpenses(newData.getExpenseList());
    }

    //// expense-level operations

    /**
     * Adds an expense to the SuperSalon.
     */
    public void addExpense(Expense e) {
        expenses.add(e);
    }

    /**
     * Replaces the given service {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the SuperSalon.
     */
    public void setExpense(Expense target, Expense editedExpense) {
        requireNonNull(editedExpense);

        expenses.setItem(target, editedExpense);
    }

    public List<Expense> filterByMonth(Predicate<Expense> predicate) {
        return expenses.stream().filter(x -> predicate.test(x)).collect(Collectors.toList());
    }

    public List<Expense> filterByYear(Predicate<Expense> predicate) {
        return expenses.stream().filter(x -> predicate.test(x)).collect(Collectors.toList());
    }

    /**
     * Removes {@code key} from this {@code ExpenseTracker}.
     * {@code key} must exist in the SuperSalon.
     */
    public void removeExpense(Expense key) {
        expenses.remove(key);
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    //// util methods

    @Override
    public String toString() {
        return "Expense Tracker:\n"
                + expenses.stream().map(Expense::toString).collect(Collectors.joining("\n"))
                + "\n Total number of expenses: " + expenses.size();

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseTracker // instanceof handles nulls
                && expenses.equals(((ExpenseTracker) other).expenses));
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenses);
    }

    /**
     * Creates a deep copy of all the expenses in the list of expenses.
     *
     * @return a list of expenses.
     */
    public ExpenseTracker deepCopy() {
        List<Expense> internalListCopy = expenses.deepCopy();
        ExpenseTracker expenseManagerCopy = new ExpenseTracker();
        expenseManagerCopy.setExpenses(internalListCopy);
        return expenseManagerCopy;
    }
}

