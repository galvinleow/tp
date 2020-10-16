package seedu.homerce.logic.commands.client;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.commons.core.index.Index;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.client.Client;

/**
 * Deletes a client identified using it's displayed index from the homerce book.
 */
public class DeleteClientCommand extends Command {

    public static final String COMMAND_WORD = "deletecli";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the client identified by the index number used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_CLIENT_SUCCESS = "Deleted Client: %1$s";

    private final Index targetIndex;

    public DeleteClientCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownList = model.getFilteredClientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        }

        Client clientToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteClient(clientToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_CLIENT_SUCCESS, clientToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteClientCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteClientCommand) other).targetIndex)); // state check
    }
}
