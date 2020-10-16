package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_ISFIXED;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.expense.Expense;

/**
 * Adds an expense to GrAB3.
 */
public class AddExpenseCommand extends Command {

    public static final String COMMAND_WORD = "addexp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an expense to GrAB3. "
            + "Parameters: "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_ISFIXED + "IS_FIXED "
            + PREFIX_AMOUNT + "VALUE "
            + PREFIX_DATE + "DATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DESCRIPTION + "Conditioner "
            + PREFIX_ISFIXED + "n "
            + PREFIX_AMOUNT + "15.00 "
            + PREFIX_DATE + "29-01-2020 "
            + PREFIX_TAG + "hairsupplies ";

    public static final String MESSAGE_SUCCESS = "New expense added: %1$s";

    private final Expense toAdd;

    /**
     * Creates an AddExpenseCommand to add the specified {@code Expense}
     */
    public AddExpenseCommand(Expense expense) {
        requireNonNull(expense);
        toAdd = expense;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);

        model.addExpense(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
