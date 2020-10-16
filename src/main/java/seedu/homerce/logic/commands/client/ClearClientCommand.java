package seedu.homerce.logic.commands.client;

import static java.util.Objects.requireNonNull;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.manager.ClientManager;

/**
 * Clears the homerce book.
 */
public class ClearClientCommand extends Command {

    public static final String COMMAND_WORD = "clearcli";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.setClientManager(new ClientManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
