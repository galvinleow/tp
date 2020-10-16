package seedu.homerce;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.homerce.commons.core.Config;
import seedu.homerce.commons.core.LogsCenter;
import seedu.homerce.commons.core.Version;
import seedu.homerce.commons.exceptions.DataConversionException;
import seedu.homerce.commons.util.ConfigUtil;
import seedu.homerce.commons.util.StringUtil;
import seedu.homerce.logic.Logic;
import seedu.homerce.logic.LogicManager;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.ModelManager;
import seedu.homerce.model.ReadOnlyUserPrefs;
import seedu.homerce.model.UserPrefs;
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
import seedu.homerce.model.util.SampleDataUtil;
import seedu.homerce.storage.JsonUserPrefsStorage;
import seedu.homerce.storage.Storage;
import seedu.homerce.storage.StorageManager;
import seedu.homerce.storage.UserPrefsStorage;
import seedu.homerce.storage.appointment.AppointmentStorage;
import seedu.homerce.storage.appointment.JsonAppointmentStorage;
import seedu.homerce.storage.client.ClientStorage;
import seedu.homerce.storage.client.JsonClientStorage;
import seedu.homerce.storage.expense.ExpenseStorage;
import seedu.homerce.storage.expense.JsonExpenseStorage;
import seedu.homerce.storage.revenue.JsonRevenueStorage;
import seedu.homerce.storage.revenue.RevenueStorage;
import seedu.homerce.storage.service.JsonServiceStorage;
import seedu.homerce.storage.service.ServiceStorage;
import seedu.homerce.ui.Ui;
import seedu.homerce.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected HistoryManager historyManager;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing Homerce ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        ClientStorage clientStorage = new JsonClientStorage(userPrefs.getClientManagerFilePath());
        ServiceStorage serviceStorage = new JsonServiceStorage(userPrefs.getServiceStorageFilePath());
        RevenueStorage revenueStorage = new JsonRevenueStorage(userPrefs.getRevenueStorageFilePath());
        ExpenseStorage expenseStorage = new JsonExpenseStorage(userPrefs.getExpenseStorageFilePath());
        AppointmentStorage appointmentStorage = new JsonAppointmentStorage(userPrefs.getAppointmentStorageFilePath());

        storage = new StorageManager(userPrefsStorage, clientStorage, serviceStorage, revenueStorage,
            expenseStorage, appointmentStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        historyManager = new HistoryManager();

        logic = new LogicManager(model, storage, historyManager);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s homerce and {@code userPrefs}. <br>
     * The data from the sample homerce will be used instead if {@code storage}'s homerce is not found,
     * or an empty homerce will be used instead if errors occur when reading {@code storage}'s homerce.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        ReadOnlyClientManager clientManager = initClientManager(storage);
        ReadOnlyServiceManager serviceManager = initServiceManager(storage);
        ReadOnlyAppointmentManager appointmentManager = initAppointmentManager(storage);
        ReadOnlyRevenueTracker revenueTracker = initRevenueTracker(storage);
        ReadOnlyExpenseTracker expenseTracker = initExpenseTracker(storage);

        return new ModelManager(userPrefs, clientManager, serviceManager, revenueTracker,
                expenseTracker, appointmentManager);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Homerce");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    /**
     *
     */
    private ReadOnlyClientManager initClientManager(Storage storage) {
        ReadOnlyClientManager clientManager;
        try {
            Optional<ReadOnlyClientManager> clientManagerOptional = storage.readClientManager();
            if (!clientManagerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample ServiceManager");
            }
            clientManager = clientManagerOptional.orElseGet(SampleDataUtil::getSampleClientManager);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty ServiceManager");
            clientManager = new ClientManager();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ServiceManager");
            clientManager = new ClientManager();
        }
        return clientManager;
    }

    /**
     * Returns a {@code ReadOnlyServiceManager} with the data from {@code storage}'s services.
     * The data from the sample services will be used instead if {@code storage}'s service manager is not found,
     * or an empty service manager will be used instead if errors occur when reading {@code storage}'s service manager.
     */
    private ReadOnlyServiceManager initServiceManager(Storage storage) {
        ReadOnlyServiceManager serviceManager;
        try {
            Optional<ReadOnlyServiceManager> serviceManagerOptional = storage.readServiceManager();
            if (!serviceManagerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample ServiceManager");
            }
            serviceManager = serviceManagerOptional.orElseGet(SampleDataUtil::getSampleServiceManager);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty ServiceManager");
            serviceManager = new ServiceManager();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ServiceManager");
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }

    /**
     * Returns a {@code ReadOnlyRevenueTracker} with the data from {@code storage}'s revenue.
     * The data from the sample revenues will be used instead if {@code storage}'s revenue tracker is not found,
     * or an empty revenue tracker will be used instead if errors occur when reading {@code storage}'s revenue tracker.
     */
    private ReadOnlyRevenueTracker initRevenueTracker(Storage storage) {
        ReadOnlyRevenueTracker revenueTracker;
        try {
            Optional<ReadOnlyRevenueTracker> revenueTrackerOptional = storage.readRevenueTracker();
            if (!revenueTrackerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample RevenueTracker");
            }
            revenueTracker = revenueTrackerOptional.orElseGet(SampleDataUtil::getSampleRevenueTracker);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty RevenueTracker");
            revenueTracker = new RevenueTracker();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty RevenueTracker");
            revenueTracker = new RevenueTracker();
        }
        return revenueTracker;
    }

    /**
     * Returns a {@code ReadOnlyExpenseTracker} with the data from {@code storage}'s services.
     * The data from the sample expenses will be used instead if {@code storage}'s expenses tracker is not found,
     * or an empty expense tracker will be used instead if errors occur when reading {@code storage}'s expense tracker.
     */
    private ReadOnlyExpenseTracker initExpenseTracker(Storage storage) {
        ReadOnlyExpenseTracker expenseTracker;
        try {
            Optional<ReadOnlyExpenseTracker> expenseTrackerOptional = storage.readExpenseTracker();
            if (!expenseTrackerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample ExpenseTracker");
            }
            expenseTracker = expenseTrackerOptional.orElseGet(SampleDataUtil::getSampleExpenseTracker);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty ExpenseTracker");
            expenseTracker = new ExpenseTracker();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ExpenseTracker");
            expenseTracker = new ExpenseTracker();
        }
        return expenseTracker;
    }


    /**
     * Returns a {@code ReadOnlyAppointmentManager} with the data from {@code storage}'s appointments.
     * The data from the sample appointments will be used instead if {@code storage}'s appointment manager is not found,
     * or an empty appointment manager will be used instead if errors occur when reading {@code storage}'s
     * appointment manager.
     */
    private ReadOnlyAppointmentManager initAppointmentManager(Storage storage) {
        ReadOnlyAppointmentManager appointmentManager;
        try {
            Optional<ReadOnlyAppointmentManager> appointmentManagerOptional = storage.readAppointmentManager();
            if (!appointmentManagerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample AppointmentManager");
            }
            appointmentManager = appointmentManagerOptional.orElseGet(SampleDataUtil::getSampleAppointmentManager);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AppointmentManager");
            appointmentManager = new AppointmentManager();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AppointmentManager");
            appointmentManager = new AppointmentManager();
        }
        return appointmentManager;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Homerce " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Homerce ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
