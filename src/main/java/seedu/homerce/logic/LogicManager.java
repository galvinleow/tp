package seedu.homerce.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.homerce.commons.core.GuiSettings;
import seedu.homerce.commons.core.LogsCenter;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.logic.parser.HomerceParser;
import seedu.homerce.logic.parser.exceptions.ParseException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.appointment.Appointment;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.manager.ReadOnlyClientManager;
import seedu.homerce.model.revenue.Revenue;
import seedu.homerce.model.service.Service;
import seedu.homerce.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {

    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final HomerceParser homerceParser;
    private HistoryManager historyManager;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage, HistoryManager historyManager) {
        this.model = model;
        this.storage = storage;
        this.historyManager = historyManager;
        homerceParser = new HomerceParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = homerceParser.parseCommand(commandText);

        // Model has not been updated by this point
        historyManager.addToHistory(model, command);

        commandResult = command.execute(model, historyManager);

        try {
            storage.saveClientManager(model.getClientManager());
            storage.saveServiceManager(model.getServiceManager());
            storage.saveRevenueTracker(model.getRevenueTracker());
            storage.saveExpenseTracker(model.getExpenseTracker());
            storage.saveAppointmentManager(model.getAppointmentManager());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyClientManager getClientManager() {
        return model.getClientManager();
    }

    @Override
    public ObservableList<Client> getFilteredClientList() {
        return model.getFilteredClientList();
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return model.getFilteredExpenseList();
    }

    @Override
    public Path getClientManagerFilePath() {
        return model.getClientManagerFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ObservableList<Service> getFilteredServiceList() {
        return model.getFilteredServiceList();
    }

    @Override
    public ObservableList<Revenue> getFilteredRevenueList() {
        return model.getFilteredRevenueList();
    }

    @Override
    public ObservableList<Appointment> getFilteredAppointmentList() {
        return model.getFilteredAppointmentList();
    }
}
