package seedu.homerce.logic.commands.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_SERVICE_CODE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_TIME_OF_DAY;
import static seedu.homerce.model.Model.PREDICATE_SHOW_ALL_APPOINTMENTS;

import java.util.List;
import java.util.Optional;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.commons.core.index.Index;
import seedu.homerce.commons.util.CollectionUtil;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.appointment.Appointment;
import seedu.homerce.model.appointment.TimeOfDay;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.client.Phone;
import seedu.homerce.model.service.Service;
import seedu.homerce.model.service.ServiceCode;
import seedu.homerce.model.util.attributes.Date;

/**
 * Edits the details of an existing client in Homerce.
 */
public class EditAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "editapt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the appointment identified "
        + "by the index number used in the displayed appointment list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "[" + PREFIX_DATE + "DATE] "
        + "[" + PREFIX_TIME_OF_DAY + "TIME] "
        + "[" + PREFIX_PHONE + "PHONE] "
        + "[" + PREFIX_SERVICE_SERVICE_CODE + "SERVICE_CODE]\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_PHONE + "91234567 "
        + PREFIX_SERVICE_SERVICE_CODE + "SC002";

    public static final String MESSAGE_EDIT_APPOINTMENT_SUCCESS = "Edited Appointment: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the homerce book.";
    public static final String MESSAGE_INVALID_PHONE = "The phone number does not refer to an existing client.";
    public static final String MESSAGE_INVALID_SERVICE_CODE = "The service code specified does not exist in Homerce.";

    private final Index index;
    private final EditAppointmentCommand.EditAppointmentDescriptor editAppointmentDescriptor;

    /**
     * @param index                     of the client in the filtered appointment list to edit
     * @param editAppointmentDescriptor details to edit the appointment with
     */
    public EditAppointmentCommand(Index index,
                                  EditAppointmentCommand.EditAppointmentDescriptor editAppointmentDescriptor) {
        requireNonNull(index);
        requireNonNull(editAppointmentDescriptor);

        this.index = index;
        this.editAppointmentDescriptor =
            new EditAppointmentCommand.EditAppointmentDescriptor(editAppointmentDescriptor);
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }
        Appointment appointmentToEdit = lastShownList.get(index.getZeroBased());
        Appointment editedAppointment = createEditedAppointment(appointmentToEdit, editAppointmentDescriptor, model);

        if (!appointmentToEdit.isSame(editedAppointment) && model.hasAppointment(editedAppointment)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

        model.setAppointment(appointmentToEdit, editedAppointment);
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_APPOINTMENT_SUCCESS, editedAppointment));
    }

    /**
     * Creates and returns an {@code Appointment} with the details of {@code appointmentToEdit}
     * edited with {@code editAppointmentDescriptor}.
     */
    private static Appointment createEditedAppointment(
        Appointment appointmentToEdit,
        EditAppointmentCommand.EditAppointmentDescriptor editAppointmentDescriptor,
        Model model) throws CommandException {
        assert appointmentToEdit != null;

        Date updatedDate = editAppointmentDescriptor.getDate().orElse(appointmentToEdit.getAppointmentDate());
        TimeOfDay updatedTimeOfDay =
            editAppointmentDescriptor.getTimeOfDay().orElse(appointmentToEdit.getAppointmentTime());
        Phone updatedPhone = editAppointmentDescriptor.getPhone().orElse(appointmentToEdit.getClient().getPhone());
        ServiceCode updatedServiceCode = editAppointmentDescriptor
            .getServiceCode().orElse(appointmentToEdit.getService().getServiceCode());
        if (!model.checkClientWithPhone(updatedPhone)) {
            throw new CommandException(MESSAGE_INVALID_PHONE);
        } else if (!model.hasService(updatedServiceCode)) {
            throw new CommandException(MESSAGE_INVALID_SERVICE_CODE);
        }
        Client updatedClient = model.getClientByPhone(updatedPhone);
        Service updatedService = model.getServiceByServiceCode(updatedServiceCode);
        return new Appointment(updatedDate, updatedTimeOfDay, updatedClient, updatedService);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditAppointmentCommand)) {
            return false;
        }

        // state check
        EditAppointmentCommand e = (EditAppointmentCommand) other;
        return index.equals(e.index)
            && editAppointmentDescriptor.equals(e.editAppointmentDescriptor);
    }

    /**
     * Stores the details to edit the appointment with. Each non-empty field value will replace the
     * corresponding field value of the appointment.
     */
    public static class EditAppointmentDescriptor {
        private Date date;
        private TimeOfDay timeOfDay;
        private Phone phone;
        private ServiceCode serviceCode;

        public EditAppointmentDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditAppointmentDescriptor(EditAppointmentCommand.EditAppointmentDescriptor toCopy) {
            setDate(toCopy.date);
            setPhone(toCopy.phone);
            setTimeOfDay(toCopy.timeOfDay);
            setServiceCode(toCopy.serviceCode);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(date, phone, timeOfDay, serviceCode);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setTimeOfDay(TimeOfDay timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

        public Optional<TimeOfDay> getTimeOfDay() {
            return Optional.ofNullable(timeOfDay);
        }

        public void setServiceCode(ServiceCode serviceCode) {
            this.serviceCode = serviceCode;
        }

        public Optional<ServiceCode> getServiceCode() {
            return Optional.ofNullable(serviceCode);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditAppointmentCommand.EditAppointmentDescriptor)) {
                return false;
            }

            // state check
            EditAppointmentCommand.EditAppointmentDescriptor e =
                (EditAppointmentCommand.EditAppointmentDescriptor) other;

            return getDate().equals(e.getDate())
                && getPhone().equals(e.getPhone())
                && getTimeOfDay().equals(e.getTimeOfDay())
                && getServiceCode().equals(e.getServiceCode());
        }
    }
}
