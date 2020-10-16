package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.manager.ExpenseTracker;

/**
 * Clears the expense list.
 */
public class ClearExpenseCommand extends Command {

    public static final String COMMAND_WORD = "clearexp";
    public static final String MESSAGE_SUCCESS = "Expense List has been cleared!";


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.setExpenseTracker(new ExpenseTracker());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
