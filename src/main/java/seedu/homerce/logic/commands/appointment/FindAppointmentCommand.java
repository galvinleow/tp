package seedu.homerce.logic.commands.appointment;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.appointment.Appointment;

public class FindAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "findapt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all appointments of which its details contains "
        + "exactly one of the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
        + "Parameters: [p/PHONE_NUMBER]* [n/NAME]* [dt/DATE]* [s/SERVICE_CODE]* [m/MONTH]*\n"
        + "Example: " + COMMAND_WORD + " p/98429700";

    private final Predicate<Appointment> predicate;

    public FindAppointmentCommand(Predicate<Appointment> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredAppointmentList(predicate);
        return new CommandResult(
            String.format(Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, model.getFilteredAppointmentList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof FindAppointmentCommand // instanceof handles nulls
            && predicate.equals(((FindAppointmentCommand) other).predicate)); // state check
    }
}
