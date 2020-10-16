package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.model.Model.PREDICATE_SHOW_ALL_EXPENSES;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;

/**
 * Lists all expenses in GrAB3 to the user.
 */
public class ListExpenseCommand extends Command {

    public static final String COMMAND_WORD = "listexp";

    public static final String MESSAGE_SUCCESS = "Listed all expenses";


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
