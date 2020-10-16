package seedu.homerce.logic.commands.appointment;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.commons.core.index.Index;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.appointment.Appointment;

/**
 * Deletes an appointment identified using it's displayed index from the homerce book.
 */
public class DeleteAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "deleteapt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the appointment identified by the index number used in the displayed appointment list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS = "Deleted Appointment: %1$s";

    private final Index targetIndex;

    public DeleteAppointmentCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }

        Appointment appointmentToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteAppointment(appointmentToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointmentToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteAppointmentCommand // instanceof handles nulls
            && targetIndex.equals(((DeleteAppointmentCommand) other).targetIndex)); // state check
    }

}
