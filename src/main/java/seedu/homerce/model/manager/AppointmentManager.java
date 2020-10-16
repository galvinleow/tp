package seedu.homerce.model.manager;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.homerce.model.appointment.Appointment;
import seedu.homerce.model.util.uniquelist.UniqueList;

/**
 * Wraps all data at the AppointmentManager level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AppointmentManager implements ReadOnlyAppointmentManager {
    private final UniqueList<Appointment> appointments;

    public AppointmentManager() {
        this.appointments = new UniqueList<>();
    }

    /**
     * Creates an AppointmentManager using the Appointments in the {@code toBeCopied}
     */
    public AppointmentManager(ReadOnlyAppointmentManager toBeCopied) {
        this.appointments = new UniqueList<>();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Resets the existing data of this {@code AppointmentManager} with {@code newData}.
     */
    public void resetData(ReadOnlyAppointmentManager newData) {
        requireNonNull(newData);
        setAppointments(newData.getAppointmentList());
    }

    //// appointment-level operations

    /**
     * Returns true if a appointment with the same identity as {@code appointment} exists in the SuperSalon.
     */
    public boolean hasAppointment(Appointment appointment) {
        requireNonNull(appointment);
        return appointments.contains(appointment);
    }

    /**
     * Adds a appointment to the SuperSalon.
     * The appointment must not already exist in SuperSalon.
     */
    public void addAppointment(Appointment p) {
        appointments.add(p);
    }

    /**
     * Replaces the contents of the appointment list with {@code appointments}.
     * {@code appointments} must not contain duplicate appointments.
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments.setItems(appointments);
    }

    /**
     * Replaces the given appointment {@code target} in the list with {@code editedAppointment}.
     * {@code target} must exist in the SuperSalon.
     * The appointment identity of {@code editedAppointment} must not be the same as another existing appointment
     * in the SuperSalon.
     */
    public void setAppointments(Appointment target, Appointment editedAppointment) {
        requireNonNull(editedAppointment);
        appointments.setItem(target, editedAppointment);
    }

    /**
     * Removes {@code appointmentToRemove} from this {@code AppointmentManager}.
     * {@code appointmentToRemove} must exist in SuperSalon.
     */
    public void removeAppointment(Appointment appointmentToRemove) {
        appointments.remove(appointmentToRemove);
    }

    //// util methods

    @Override
    public String toString() {
        return "Appointment Manager:\n"
                + appointments.stream().map(Appointment::toString).collect(Collectors.joining("\n"))
                + "\n Total number of appointments: " + appointments.size();
    }

    @Override
    public ObservableList<Appointment> getAppointmentList() {
        return appointments.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AppointmentManager // instanceof handles nulls
                && appointments.equals(((AppointmentManager) other).appointments));
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointments);
    }

    /**
     * Creates a deep copy of all the appointments in the unique list of appointments.
     *
     * @return an appointment manager with a list of deep copied appointments.
     */
    public AppointmentManager deepCopy() {
        List<Appointment> internalListCopy = appointments.deepCopy();
        AppointmentManager appointmentManagerCopy = new AppointmentManager();
        appointmentManagerCopy.setAppointments(internalListCopy);
        return appointmentManagerCopy;
    }
}
