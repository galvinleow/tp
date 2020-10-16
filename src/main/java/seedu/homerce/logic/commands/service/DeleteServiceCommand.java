package seedu.homerce.logic.commands.service;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.commons.core.index.Index;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.service.Service;

/**
 * Deletes a service identified using it's displayed index from SuperSalon's service list.
 */
public class DeleteServiceCommand extends Command {

    public static final String COMMAND_WORD = "deletesvc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the service identified by the index number used in the displayed service list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_SERVICE_SUCCESS = "Deleted Service: %1$s";

    private final Index targetIndex;

    public DeleteServiceCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        List<Service> lastShownList = model.getFilteredServiceList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_SERVICES_INVALID_SERVICE_DISPLAYED_INDEX);
        }

        Service serviceToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteService(serviceToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteServiceCommand // instanceof handles nulls
            && targetIndex.equals(((DeleteServiceCommand) other).targetIndex)); // state check
    }
}
