package seedu.homerce.logic.commands.revenue;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.revenue.Revenue;

public class ClearRevenueCommand extends Command {

    public static final String COMMAND_WORD = "clearrev";
    public static final String MESSAGE_SUCCESS = "Revenue List has been cleared!";


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.setRevenues(new ArrayList<Revenue>());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
