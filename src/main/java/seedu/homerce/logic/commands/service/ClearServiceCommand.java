package seedu.homerce.logic.commands.service;

import static java.util.Objects.requireNonNull;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.manager.ServiceManager;

/**
 * Clears the homerce book.
 */
public class ClearServiceCommand extends Command {

    public static final String COMMAND_WORD = "clearsvc";
    public static final String MESSAGE_SUCCESS = "All services has been cleared!";


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.setServiceManager(new ServiceManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
