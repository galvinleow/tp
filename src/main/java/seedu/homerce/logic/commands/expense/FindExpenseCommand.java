package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_ISFIXED;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.expense.Expense;

/**
 * Finds and lists all clients in homerce book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindExpenseCommand extends Command {

    public static final String COMMAND_WORD = "findexp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all expenses by either their "
            + "description, date, is fixed, or tag, and displays them as a list with index numbers.\n"
            + "Parameters: "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_ISFIXED + "ISFIXED] "
            + "[" + PREFIX_TAG + "TAG] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DESCRIPTION + "Conditioner ";

    private final Predicate<Expense> predicate;

    public FindExpenseCommand(Predicate<Expense> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredExpenseList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW, model.getFilteredExpenseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindExpenseCommand // instanceof handles nulls
                && predicate.equals(((FindExpenseCommand) other).predicate)); // state check
    }
}
