package seedu.homerce.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.homerce.commons.core.GuiSettings;
import seedu.homerce.model.appointment.Appointment;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.client.Phone;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.manager.ReadOnlyAppointmentManager;
import seedu.homerce.model.manager.ReadOnlyClientManager;
import seedu.homerce.model.manager.ReadOnlyExpenseTracker;
import seedu.homerce.model.manager.ReadOnlyRevenueTracker;
import seedu.homerce.model.manager.ReadOnlyServiceManager;
import seedu.homerce.model.revenue.Revenue;
import seedu.homerce.model.service.Service;
import seedu.homerce.model.service.ServiceCode;

/**
 * The API of the Model component.
 */
public interface Model {

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Client> PREDICATE_SHOW_ALL_CLIENTS = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Service> PREDICATE_SHOW_ALL_SERVICES = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Revenue> PREDICATE_SHOW_ALL_REVENUE = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Appointment> PREDICATE_SHOW_ALL_APPOINTMENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' homerce book file path.
     */
    Path getClientManagerFilePath();

    /**
     * Sets the user prefs' homerce book file path.
     */
    void setClientManagerFilePath(Path clientManagerFilePath);

    /**
     * Replaces homerce book data with the data in {@code clientManager}.
     */
    void setClientManager(ReadOnlyClientManager clientManager);

    /**
     * Returns the ClientManager
     */
    ReadOnlyClientManager getClientManager();

    /**
     * Returns true if a client with the same identity as {@code client} exists in the homerce book.
     */
    boolean hasClient(Client client);

    /**
     * Returns true if a client with the same phone number as {@code phone} exists in the homerce book.
     */
    boolean checkClientWithPhone(Phone phone);

    /**
     * Deletes the given client.
     * The client must exist in the homerce book.
     */
    void deleteClient(Client target);

    /**
     * Adds the given client.
     * {@code client} must not already exist in the homerce book.
     */
    void addClient(Client client);

    /**
     * Gets the client based on provided phone number.
     * {@code phone} must exist in the homerce book.
     */
    Client getClientByPhone(Phone phone);

    /**
     * Replaces the given client {@code target} with {@code editedClient}.
     * {@code target} must exist in the homerce book.
     * The client identity of {@code editedClient} must not be the same as another existing client in the homerce book.
     */
    void setClient(Client target, Client editedClient);

    /**
     * Returns an unmodifiable view of the filtered client list
     */
    ObservableList<Client> getFilteredClientList();

    /**
     * Updates the filter of the filtered client list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredClientList(Predicate<Client> predicate);

    /**
     * Deletes the given expense.
     * The expense must exist in GrAB3.
     */
    void deleteExpense(Expense target);

    /**
     * Adds the given expense.
     */
    void addExpense(Expense expense);

    /**
     * Replaces the given expense {@code target} with {@code editedExpense}.
     * {@code target} must exist in the GrAB3.
     */
    void setExpense(Expense target, Expense editedExpense);

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     */
    void setExpenses(List<Expense> expenses);

    /**
     * Returns a List of expenses filtered by month.
     */
    List<Expense> filterExpenseByMonth(Predicate<Expense> predicate);

    /**
     * Returns a List of expenses filtered by year.
     */
    List<Expense> filterExpenseByYear(Predicate<Expense> predicate);

    /**
     * Returns an unmodifiable view of the filtered expense list
     */
    ObservableList<Expense> getFilteredExpenseList();

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredExpenseList(Predicate<Expense> predicate);

    ReadOnlyExpenseTracker getExpenseTracker();

    /**
     * Replaces expenseTracker data with the data in {@code expenseTracker}.
     */
    void setExpenseTracker(ReadOnlyExpenseTracker expenseTracker);

    // ====================== ServiceManager ========================

    /**
     * Adds the given service.
     */
    void addService(Service toAdd);

    /**
     * Returns true if a service with the same service code as {@code code} exists in SuperSalon.
     */
    boolean hasService(ServiceCode code);

    Service getServiceByServiceCode(ServiceCode serviceCode);

    /**
     * Deletes the given Service.
     * The Service must exist in SuperSalon.
     */
    void deleteService(Service target);

    /**
     * Replaces the given service {@code target} with {@code editedService}.
     * {@code target} must exist in SuperSalon's service list.
     * The service identity of {@code editedService} must not be the same as another existing service in SuperSalon.
     */
    void setService(Service target, Service editedService);

    /**
     * Updates the filter of the filtered service list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredServiceList(Predicate<Service> predicate);

    ObservableList<Service> getFilteredServiceList();

    ReadOnlyServiceManager getServiceManager();

    /**
     * Replaces serviceManager data with the data in {@code serviceManager}.
     */
    void setServiceManager(ReadOnlyServiceManager serviceManager);

    // ====================== Revenue Tracker ========================

    /**
     * Adds the given revenue.
     */
    void addRevenue(Revenue toAdd);

    /**
     * Deletes the given revenue.
     * The revenue must exist in GrAB3.
     */
    void deleteRevenue(Revenue target);

    /**
     * Replaces the contents of the revenue list with {@code expenses}.
     */
    void setRevenues(List<Revenue> revenues);

    /**
     * Returns a List of revenue filtered by month.
     */
    List<Revenue> filterRevenueByYear(Predicate<Revenue> predicate);

    /**
     * Returns a list of revenue filtered by year
     */
    List<Revenue> filterRevenueByMonth(Predicate<Revenue> predicate);

    void updateFilteredRevenueList(Predicate<Revenue> predicate);

    ObservableList<Revenue> getFilteredRevenueList();

    ReadOnlyRevenueTracker getRevenueTracker();

    void setRevenueTracker(ReadOnlyRevenueTracker revenueTracker);


    // ====================== AppointmentManager ========================
    /**
     * Adds the given appointment.
     */
    void addAppointment(Appointment toAdd);

    /**
     * Deletes the given appointment.
     */
    void deleteAppointment(Appointment toDelete);

    /**
     * Updates the filter of the filtered appointment list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredAppointmentList(Predicate<Appointment> predicate);

    /** Returns an unmodifiable view of the filtered appointment list */
    ObservableList<Appointment> getFilteredAppointmentList();

    /** Returns the appointment manager. */
    ReadOnlyAppointmentManager getAppointmentManager();

    /**
     * Replaces the given appointment {@code target} with {@code editedAppointment}.
     * {@code target} must exist in the SuperSalon.
     */
    void setAppointment(Appointment target, Appointment editedAppointment);

    /**
     * Replaces the contents of the appointment list with {@code appointment}.
     */
    void setAppointment(List<Appointment> appointment);

    /**
     * Checks if Appointment is stored in Appointment Manager.
     */
    boolean hasAppointment(Appointment appointment);

    void setAppointmentManager(ReadOnlyAppointmentManager appointmentManager);

    // ====================== HistoryManager ========================
    void replaceModel(Model previousModel);

    Model deepCopy();
}
