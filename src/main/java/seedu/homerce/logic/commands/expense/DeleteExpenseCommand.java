package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.commons.core.index.Index;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.expense.Expense;

/**
 * Deletes an expense identified using it's displayed index from GrAB3.
 */
public class DeleteExpenseCommand extends Command {

    public static final String COMMAND_WORD = "deleteexp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the expense identified by the index number used in the displayed expense list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EXPENSE_SUCCESS = "Deleted Expense: %1$s";

    private final Index targetIndex;

    public DeleteExpenseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        List<Expense> lastShownList = model.getFilteredExpenseList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
        }

        Expense expenseToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteExpense(expenseToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_EXPENSE_SUCCESS, expenseToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.homerce.logic.commands.expense.DeleteExpenseCommand
                && targetIndex.equals(((seedu.homerce.logic.commands.expense.DeleteExpenseCommand)
                other).targetIndex)); // state check
    }
}
