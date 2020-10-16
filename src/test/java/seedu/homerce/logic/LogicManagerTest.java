package seedu.homerce.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.homerce.commons.core.Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX;
import static seedu.homerce.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.homerce.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.homerce.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.homerce.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.homerce.testutil.Assert.assertThrows;
import static seedu.homerce.testutil.TypicalClients.AMY;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.client.AddClientCommand;
import seedu.homerce.logic.commands.client.ListClientCommand;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.logic.parser.exceptions.ParseException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.ModelManager;
import seedu.homerce.model.UserPrefs;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.manager.AppointmentManager;
import seedu.homerce.model.manager.ExpenseTracker;
import seedu.homerce.model.manager.ReadOnlyClientManager;
import seedu.homerce.model.manager.RevenueTracker;
import seedu.homerce.model.manager.ServiceManager;
import seedu.homerce.storage.JsonUserPrefsStorage;
import seedu.homerce.storage.StorageManager;
import seedu.homerce.storage.appointment.AppointmentStorage;
import seedu.homerce.storage.appointment.JsonAppointmentStorage;
import seedu.homerce.storage.client.JsonClientStorage;
import seedu.homerce.storage.expense.ExpenseStorage;
import seedu.homerce.storage.expense.JsonExpenseStorage;
import seedu.homerce.storage.revenue.JsonRevenueStorage;
import seedu.homerce.storage.revenue.RevenueStorage;
import seedu.homerce.storage.service.JsonServiceStorage;
import seedu.homerce.storage.service.ServiceStorage;
import seedu.homerce.testutil.ClientBuilder;

public class LogicManagerTest {

    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonClientStorage clientStorage =
            new JsonClientStorage(temporaryFolder.resolve("clients.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        ServiceStorage serviceStorage = new JsonServiceStorage(temporaryFolder.resolve("services.json"));
        RevenueStorage revenueStorage = new JsonRevenueStorage(temporaryFolder.resolve("revenue.json"));
        ExpenseStorage expenseStorage = new JsonExpenseStorage(temporaryFolder.resolve("expenses.json"));
        AppointmentStorage appointmentStorage = new JsonAppointmentStorage(
                temporaryFolder.resolve("appointments.json"));

        StorageManager storage = new StorageManager(userPrefsStorage, clientStorage, serviceStorage,
            revenueStorage, expenseStorage, appointmentStorage);

        HistoryManager historyManager = new HistoryManager();

        logic = new LogicManager(model, storage, historyManager);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "deletecli 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListClientCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListClientCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {

        JsonClientStorage clientStorage =
            new JsonClientIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionClients.json"));
        JsonUserPrefsStorage userPrefsStorage =
            new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        ServiceStorage serviceStorage = new JsonServiceStorage(temporaryFolder.resolve("ioExceptionServices.json"));
        RevenueStorage revenueStorage = new JsonRevenueStorage(temporaryFolder.resolve("ioExceptionRevenue.json"));
        ExpenseStorage expenseStorage = new JsonExpenseStorage(temporaryFolder.resolve("ioExceptionExpenses.json"));
        AppointmentStorage appointmentStorage = new JsonAppointmentStorage(
                temporaryFolder.resolve("appointments.json"));

        StorageManager storage = new StorageManager(userPrefsStorage, clientStorage, serviceStorage,
            revenueStorage, expenseStorage, appointmentStorage);

        HistoryManager historyManager = new HistoryManager();

        logic = new LogicManager(model, storage, historyManager);

        // Execute add command
        String addCommand = AddClientCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        Client expectedClient = new ClientBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addClient(expectedClient);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredClientList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredClientList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(new UserPrefs(), model.getClientManager(),
                new ServiceManager(), new RevenueTracker(), new ExpenseTracker(), new AppointmentManager());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonClientIoExceptionThrowingStub extends JsonClientStorage {

        private JsonClientIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveClientManager(ReadOnlyClientManager clientManager, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
