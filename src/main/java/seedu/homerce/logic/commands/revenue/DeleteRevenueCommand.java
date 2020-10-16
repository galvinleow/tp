package seedu.homerce.logic.commands.revenue;

import static java.util.Objects.requireNonNull;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.revenue.Revenue;

public class DeleteRevenueCommand extends Command {

    public static final String MESSAGE_DELETE_REVENUE_SUCCESS = "Deleted Revenue: %1$s";
    private final Revenue targetRevenue;

    public DeleteRevenueCommand(Revenue targetRevenue) {
        this.targetRevenue = targetRevenue;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        model.deleteRevenue(targetRevenue);
        return new CommandResult(String.format(MESSAGE_DELETE_REVENUE_SUCCESS, targetRevenue));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteRevenueCommand // instanceof handles nulls
            && targetRevenue.equals(((DeleteRevenueCommand) other).targetRevenue)); // state check
    }
}
