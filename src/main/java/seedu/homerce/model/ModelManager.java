package seedu.homerce.model;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.homerce.commons.core.GuiSettings;
import seedu.homerce.commons.core.LogsCenter;
import seedu.homerce.commons.util.CollectionUtil;
import seedu.homerce.model.appointment.Appointment;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.client.Phone;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.manager.AppointmentManager;
import seedu.homerce.model.manager.ClientManager;
import seedu.homerce.model.manager.ExpenseTracker;
import seedu.homerce.model.manager.ReadOnlyAppointmentManager;
import seedu.homerce.model.manager.ReadOnlyClientManager;
import seedu.homerce.model.manager.ReadOnlyExpenseTracker;
import seedu.homerce.model.manager.ReadOnlyRevenueTracker;
import seedu.homerce.model.manager.ReadOnlyServiceManager;
import seedu.homerce.model.manager.RevenueTracker;
import seedu.homerce.model.manager.ServiceManager;
import seedu.homerce.model.revenue.Revenue;
import seedu.homerce.model.service.Service;
import seedu.homerce.model.service.ServiceCode;

/**
 * Represents the in-memory model of the homerce book data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ClientManager clientManager; // This is ClientManager.
    private final ServiceManager serviceManager;
    private final AppointmentManager appointmentManager;
    private final RevenueTracker revenueTracker;
    private final ExpenseTracker expenseTracker;
    private final UserPrefs userPrefs;

    private final FilteredList<Client> filteredClients;
    private final FilteredList<Expense> filteredExpenses;
    private final FilteredList<Service> filteredServices;
    private final FilteredList<Revenue> filteredRevenue;
    private final FilteredList<Appointment> filteredAppointments;

    /**
     * Initializes a ModelManager with the given clientManager and userPrefs.
     */
    public ModelManager(ReadOnlyUserPrefs userPrefs, ReadOnlyClientManager clientManager,
                        ReadOnlyServiceManager serviceManager, ReadOnlyRevenueTracker revenueTracker,
                        ReadOnlyExpenseTracker expenseTracker, ReadOnlyAppointmentManager appointmentManager) {
        super();
        requireAllNonNull(clientManager, userPrefs, serviceManager, revenueTracker, expenseTracker);

        logger.fine("Initializing with Homerce: " + clientManager + " and user prefs " + userPrefs);

        this.clientManager = new ClientManager(clientManager);
        this.userPrefs = new UserPrefs(userPrefs);
        this.serviceManager = new ServiceManager(serviceManager);
        this.appointmentManager = new AppointmentManager(appointmentManager);
        this.revenueTracker = new RevenueTracker(revenueTracker);
        this.expenseTracker = new ExpenseTracker(expenseTracker);

        filteredClients = new FilteredList<>(this.clientManager.getClientList());
        filteredExpenses = new FilteredList<>(this.expenseTracker.getExpenseList());
        filteredServices = new FilteredList<>(this.serviceManager.getServiceList());
        filteredAppointments = new FilteredList<>(this.appointmentManager.getAppointmentList());
        filteredRevenue = new FilteredList<>(this.revenueTracker.getRevenueList());
    }

    /**
     * Initializes a ModelManager with the given clientManager and userPrefs.
     */
    public ModelManager(ClientManager clientManager, ServiceManager serviceManager,
                        RevenueTracker revenueTracker, ExpenseTracker expenseTracker,
                        AppointmentManager appointmentManager, UserPrefs userPrefs) {
        super();
        requireAllNonNull(clientManager, serviceManager, revenueTracker, expenseTracker);

        logger.fine("Creating deep copy of Model Manager: ");

        this.clientManager = clientManager;
        this.userPrefs = userPrefs;
        this.serviceManager = serviceManager;
        this.appointmentManager = appointmentManager;
        this.revenueTracker = revenueTracker;
        this.expenseTracker = expenseTracker;

        filteredClients = new FilteredList<>(this.clientManager.getClientList());
        filteredExpenses = new FilteredList<>(this.expenseTracker.getExpenseList());
        filteredServices = new FilteredList<>(this.serviceManager.getServiceList());
        filteredAppointments = new FilteredList<>(this.appointmentManager.getAppointmentList());
        filteredRevenue = new FilteredList<>(this.revenueTracker.getRevenueList());
    }

    /**
     * Initializes a ModelManager with the given clientManager and userPrefs.
     */
    public ModelManager() {
        this(new UserPrefs(), new ClientManager(), new ServiceManager(), new RevenueTracker(), new ExpenseTracker(),
            new AppointmentManager());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getClientManagerFilePath() {
        return userPrefs.getClientManagerFilePath();
    }

    @Override
    public void setClientManagerFilePath(Path clientManagerFilePath) {
        requireNonNull(clientManagerFilePath);
        userPrefs.setClientManagerFilePath(clientManagerFilePath);
    }

    //=========== ClientManager ================================================================================

    @Override
    public void setClientManager(ReadOnlyClientManager clientManager) {
        this.clientManager.resetData(clientManager);
    }

    @Override
    public ReadOnlyClientManager getClientManager() {
        return clientManager;
    }

    @Override
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return clientManager.hasClient(client);
    }

    /**
     * Checks if a client exists in the client manager using the client's phone number.
     */
    public boolean checkClientWithPhone(Phone phone) {
        requireAllNonNull(phone);
        return clientManager.checkClientWithPhone(phone);
    }

    @Override
    public void deleteClient(Client target) {
        clientManager.removeClient(target);
    }

    @Override
    public void addClient(Client client) {
        clientManager.addClient(client);
        updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Override
    public void setClient(Client target, Client editedClient) {
        requireAllNonNull(target, editedClient);

        clientManager.setClient(target, editedClient);
    }

    @Override
    public Client getClientByPhone(Phone phone) {
        requireAllNonNull(phone);
        return clientManager.getClientByPhone(phone);
    }

    //=========== Expense Tracker =============================================================
    @Override
    public void deleteExpense(Expense target) {
        expenseTracker.removeExpense(target);
    }

    @Override
    public void addExpense(Expense expense) {
        expenseTracker.addExpense(expense);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
    }

    @Override
    public void setExpense(Expense target, Expense editedExpense) {
        requireAllNonNull(target, editedExpense);

        expenseTracker.setExpense(target, editedExpense);
    }

    @Override
    public void setExpenses(List<Expense> expenses) {
        expenseTracker.setExpenses(expenses);
    }

    @Override
    public List<Expense> filterExpenseByYear(Predicate<Expense> predicate) {
        return expenseTracker.filterByYear(predicate);
    }

    @Override
    public List<Expense> filterExpenseByMonth(Predicate<Expense> predicate) {
        return expenseTracker.filterByMonth(predicate);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of Expenses
     * {@code versionedClientManager}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    /**
     * Replaces expenseTracker data with the data in {@code expenseTracker}.
     */
    @Override
    public void setExpenseTracker(ReadOnlyExpenseTracker expenseTracker) {
        requireNonNull(expenseTracker);
        this.expenseTracker.resetData(expenseTracker);
    }

    @Override
    public ReadOnlyExpenseTracker getExpenseTracker() {
        return this.expenseTracker;
    }

    //=========== Filtered Client List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Client} backed by the internal list of
     * {@code versionedClientManager}
     */
    @Override
    public ObservableList<Client> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public void updateFilteredClientList(Predicate<Client> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate(predicate);
    }

    //================ ServiceManager ==================
    @Override
    public void addService(Service toAdd) {
        requireNonNull(toAdd);
        serviceManager.addService(toAdd);
        updateFilteredServiceList(PREDICATE_SHOW_ALL_SERVICES);
    }

    @Override
    public void deleteService(Service target) {
        serviceManager.removeService(target);
    }

    @Override
    public void setService(Service target, Service editedService) {
        CollectionUtil.requireAllNonNull(target, editedService);
        serviceManager.setService(target, editedService);
    }

    @Override
    public boolean hasService(ServiceCode code) {
        requireAllNonNull(code);
        return serviceManager.hasService(code);
    }

    @Override
    public void updateFilteredServiceList(Predicate<Service> predicate) {
        requireNonNull(predicate);
        filteredServices.setPredicate(predicate);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Client} backed by the internal list of
     * {@code versionedClientManager}
     */
    @Override
    public ObservableList<Service> getFilteredServiceList() {
        return filteredServices;
    }

    @Override
    public ReadOnlyServiceManager getServiceManager() {
        return this.serviceManager;
    }

    /**
     * Replaces serviceManager data with the data in {@code serviceManager}.
     */
    @Override
    public void setServiceManager(ReadOnlyServiceManager serviceManager) {
        requireNonNull(serviceManager);
        this.serviceManager.resetData(serviceManager);
    }

    @Override
    public Service getServiceByServiceCode(ServiceCode serviceCode) {
        requireAllNonNull(serviceCode);
        return serviceManager.getServiceByServiceCode(serviceCode);
    }

    //=========== RevenueTracker ===============
    @Override
    public void addRevenue(Revenue toAdd) {
        requireNonNull(toAdd);
        revenueTracker.addRevenue(toAdd);
        updateFilteredRevenueList(PREDICATE_SHOW_ALL_REVENUE);
    }

    @Override
    public void deleteRevenue(Revenue target) {
        revenueTracker.removeRevenue(target);
    }

    @Override
    public void setRevenues(List<Revenue> revenues) {
        revenueTracker.setRevenues(revenues);
    }

    @Override
    public List<Revenue> filterRevenueByMonth(Predicate<Revenue> predicate) {
        return revenueTracker.filterByMonth(predicate);
    }

    @Override
    public List<Revenue> filterRevenueByYear(Predicate<Revenue> predicate) {
        return revenueTracker.filterByYear(predicate);
    }

    @Override
    public void updateFilteredRevenueList(Predicate<Revenue> predicate) {
        requireNonNull(predicate);
        filteredRevenue.setPredicate(predicate);
    }

    @Override
    public ObservableList<Revenue> getFilteredRevenueList() {
        return filteredRevenue;
    }

    @Override
    public ReadOnlyRevenueTracker getRevenueTracker() {
        return this.revenueTracker;
    }

    /**
     * Replaces revenueTracker data with the data in {@code revenueTracker}.
     */
    @Override
    public void setRevenueTracker(ReadOnlyRevenueTracker revenueTracker) {
        requireNonNull(revenueTracker);
        this.revenueTracker.resetData(revenueTracker);
    }

    //================== AppointmentManager ==================
    @Override
    public void addAppointment(Appointment toAdd) {
        requireNonNull(toAdd);
        appointmentManager.addAppointment(toAdd);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
    }

    @Override
    public void deleteAppointment(Appointment toDelete) {
        appointmentManager.removeAppointment(toDelete);
    }

    @Override
    public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
        requireNonNull(predicate);
        filteredAppointments.setPredicate(predicate);
    }

    @Override
    public ObservableList<Appointment> getFilteredAppointmentList() {
        return filteredAppointments;
    }

    @Override
    public ReadOnlyAppointmentManager getAppointmentManager() {
        return appointmentManager;
    }

    @Override
    public void setAppointment(Appointment target, Appointment editedAppointment) {
        requireAllNonNull(target, editedAppointment);
        appointmentManager.setAppointments(target, editedAppointment);
    }

    @Override
    public void setAppointment(List<Appointment> appointments) {
        appointmentManager.setAppointments(appointments);
    }

    @Override
    public boolean hasAppointment(Appointment appointment) {
        return appointmentManager.hasAppointment(appointment);
    }

    /**
     * Replaces serviceManager data with the data in {@code serviceManager}.
     */
    @Override
    public void setAppointmentManager(ReadOnlyAppointmentManager appointmentManager) {
        requireNonNull(appointmentManager);
        this.appointmentManager.resetData(appointmentManager);
    }

    //================== AppointmentManager ==================
    @Override
    public void replaceModel(Model previousModel) {
        this.setClientManager(previousModel.getClientManager());
        this.setServiceManager(previousModel.getServiceManager());
        this.setAppointmentManager(previousModel.getAppointmentManager());
        this.setRevenueTracker(previousModel.getRevenueTracker());
        this.setExpenseTracker(previousModel.getExpenseTracker());
    }

    @Override
    public Model deepCopy() {
        ClientManager clientManagerCopy = clientManager.deepCopy();
        ServiceManager serviceManagerCopy = serviceManager.deepCopy();
        RevenueTracker revenueTrackerCopy = revenueTracker.deepCopy();
        ExpenseTracker expenseTrackerCopy = expenseTracker.deepCopy();
        AppointmentManager appointmentManagerCopy = appointmentManager.deepCopy();

        return new ModelManager(clientManagerCopy, serviceManagerCopy, revenueTrackerCopy,
            expenseTrackerCopy, appointmentManagerCopy, userPrefs);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return clientManager.equals(other.clientManager)
            && userPrefs.equals(other.userPrefs)
            && filteredClients.equals(other.filteredClients)
            && filteredServices.equals(other.filteredServices)
            && filteredAppointments.equals(other.filteredAppointments)
            && filteredExpenses.equals(other.filteredExpenses);
    }
}
