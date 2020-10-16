package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_ISFIXED;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.homerce.model.Model.PREDICATE_SHOW_ALL_EXPENSES;

import java.util.List;
import java.util.Optional;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.commons.core.index.Index;
import seedu.homerce.commons.util.CollectionUtil;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.expense.IsFixed;
import seedu.homerce.model.util.attributes.Amount;
import seedu.homerce.model.util.attributes.Date;
import seedu.homerce.model.util.attributes.Description;
import seedu.homerce.model.util.attributes.Tag;

/**
 * Edits the details of an existing expense in GrAB3.
 */
public class EditExpenseCommand extends Command {

    public static final String COMMAND_WORD = "editexp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the expense identified "
            + "by the index number used in the displayed expense list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_ISFIXED + "IS_FIXED] "
            + "[" + PREFIX_AMOUNT + "VALUE] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ISFIXED + "t "
            + PREFIX_AMOUNT + "20.00";

    public static final String MESSAGE_EDIT_EXPENSE_SUCCESS = "Edited Expense: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private final Index index;
    private final EditExpenseCommand.EditExpenseDescriptor editExpenseDescriptor;

    /**
     * @param index                 of the expense in the filtered expense list to edit
     * @param editExpenseDescriptor details to edit the expense with
     */
    public EditExpenseCommand(Index index, EditExpenseCommand.EditExpenseDescriptor editExpenseDescriptor) {
        requireNonNull(index);
        requireNonNull(editExpenseDescriptor);

        this.index = index;
        this.editExpenseDescriptor = new EditExpenseCommand.EditExpenseDescriptor(editExpenseDescriptor);
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        List<Expense> lastShownList = model.getFilteredExpenseList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
        }

        Expense expenseToEdit = lastShownList.get(index.getZeroBased());
        Expense editedExpense = createEditedExpense(expenseToEdit, editExpenseDescriptor);

        if (expenseToEdit.equals(editedExpense)) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        model.setExpense(expenseToEdit, editedExpense);
        model.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        return new CommandResult(String.format(MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense));
    }

    /**
     * Creates and returns a {@code Expense} with the details of {@code expenseToEdit}
     * edited with {@code editExpenseDescriptor}.
     */
    private static Expense createEditedExpense(Expense expenseToEdit,
                                               EditExpenseCommand.EditExpenseDescriptor editExpenseDescriptor) {
        assert expenseToEdit != null;

        Description updatedDescription = editExpenseDescriptor.getDescription().orElse(expenseToEdit.getDescription());
        IsFixed updatedIsFixed = editExpenseDescriptor.getIsFixed().orElse(expenseToEdit.getIsFixed());
        Amount updatedValue = editExpenseDescriptor.getAmount().orElse(expenseToEdit.getValue());
        Date updatedDate = editExpenseDescriptor.getDate().orElse(expenseToEdit.getDate());
        Tag updatedTag = editExpenseDescriptor.getTag().orElse(expenseToEdit.getTag());

        return new Expense(updatedDescription, updatedIsFixed, updatedValue, updatedDate, updatedTag);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditExpenseCommand)) {
            return false;
        }

        // state check
        EditExpenseCommand e = (EditExpenseCommand) other;
        return index.equals(e.index)
                && editExpenseDescriptor.equals(e.editExpenseDescriptor);
    }

    /**
     * Stores the details to edit the expense with. Each non-empty field value will replace the
     * corresponding field value of the expense.
     */
    public static class EditExpenseDescriptor {
        private Description description;
        private IsFixed isFixed;
        private Amount value;
        private Date date;
        private Tag tag;

        public EditExpenseDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditExpenseDescriptor(EditExpenseCommand.EditExpenseDescriptor toCopy) {
            setDescription(toCopy.description);
            setIsFixed(toCopy.isFixed);
            setAmount(toCopy.value);
            setDate(toCopy.date);
            setTag(toCopy.tag);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(description, isFixed, value, date, tag);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setIsFixed(IsFixed isFixed) {
            this.isFixed = isFixed;
        }

        public Optional<IsFixed> getIsFixed() {
            return Optional.ofNullable(isFixed);
        }

        public void setAmount(Amount value) {
            this.value = value;
        }

        public Optional<Amount> getAmount() {
            return Optional.ofNullable(value);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTag(Tag tag) {
            this.tag = (tag != null) ? tag : null;
        }

        public Optional<Tag> getTag() {
            return (tag != null) ? Optional.ofNullable(tag) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditExpenseCommand.EditExpenseDescriptor)) {
                return false;
            }

            // state check
            EditExpenseCommand.EditExpenseDescriptor e = (EditExpenseCommand.EditExpenseDescriptor) other;

            return getDescription().equals(e.getDescription())
                    && getIsFixed().equals(e.getIsFixed())
                    && getAmount().equals(e.getAmount())
                    && getDate().equals(e.getDate())
                    && getTag().equals(e.getTag());
        }
    }
}
