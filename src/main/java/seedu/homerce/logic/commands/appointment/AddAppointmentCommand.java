package seedu.homerce.logic.commands.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_SERVICE_CODE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_TIME_OF_DAY;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.appointment.Appointment;
import seedu.homerce.model.appointment.AppointmentTemp;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.service.Service;

/**
 * Adds an appointment to SuperSalon.
 */
public class AddAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "addapt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to the homerce book. "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME_OF_DAY + "TIME "
            + PREFIX_SERVICE_SERVICE_CODE + "SERVICE_CODE "
            + PREFIX_PHONE + "PHONE" + "\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "15-2-2021 "
            + PREFIX_TIME_OF_DAY + "1430 "
            + PREFIX_SERVICE_SERVICE_CODE + "SC001 "
            + PREFIX_PHONE + "94759600";

    public static final String MESSAGE_SUCCESS = "New appointment added: %1$s";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in SuperSalon.";
    public static final String MESSAGE_INVALID_PHONE = "The phone number specified does not refer "
            + "to an existing client.";
    public static final String MESSAGE_INVALID_SERVICE_CODE = "The service code specified does"
            + " not exist in SuperSalon.";

    private final AppointmentTemp toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Client}
     */
    public AddAppointmentCommand(AppointmentTemp appointment) {
        requireNonNull(appointment);
        toAdd = appointment;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        // Check if phone number and service code are present in the model.
        if (!model.checkClientWithPhone(toAdd.getPhone())) {
            throw new CommandException(MESSAGE_INVALID_PHONE);
        } else if (!model.hasService(toAdd.getServiceCode())) {
            throw new CommandException(MESSAGE_INVALID_SERVICE_CODE);
        }
        // Create client and service with the phone number and service code.
        Client clientToAdd = model.getClientByPhone(toAdd.getPhone());
        Service serviceToAdd = model.getServiceByServiceCode(toAdd.getServiceCode());
        Appointment resultToAdd = new Appointment(
            toAdd.getAppointmentDate(), toAdd.getAppointmentTime(),
            clientToAdd, serviceToAdd
        );
        // Check if appointment is already present.
        if (model.hasAppointment(resultToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } else {
            model.addAppointment(resultToAdd);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, resultToAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAppointmentCommand // instanceof handles nulls
                && toAdd.equals(((AddAppointmentCommand) other).toAdd));
    }
}
