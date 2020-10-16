package seedu.homerce.logic.commands;

import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;

/**
 * Undo the last command that updates the state of Homerce's storage.
 *
 * A command that does not affect the state of Homerce's storage will not be considered in the undo command.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful!";

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        Model previousModel = historyManager.getPreviousState();
        if (previousModel == null) {
            throw new CommandException("Already at oldest change");
        }
        model.replaceModel(previousModel);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
