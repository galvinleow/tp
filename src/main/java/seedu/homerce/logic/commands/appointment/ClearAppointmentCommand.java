package seedu.homerce.logic.commands.appointment;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;

/**
 * Clears the appointment list.
 */
public class ClearAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "clearapt";
    public static final String MESSAGE_SUCCESS = "Appointment list has been cleared!";

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.setAppointment(new ArrayList<>());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
