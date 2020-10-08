package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.manager.ReadOnlyRevenueTracker;
import seedu.address.model.manager.ReadOnlyServiceManager;
import seedu.address.storage.revenue.RevenueStorage;
import seedu.address.storage.service.ServiceStorage;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private ServiceStorage serviceStorage;
    private UserPrefsStorage userPrefsStorage;
    private RevenueStorage revenueStorage;

    /**
     * Creates a {@code StorageManager} with the given
     * {@code AddressBookStorage}, {@code UserPrefStorage}, {@code ServiceStorage} and {@code RevenueStorage}.
     */
    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage,
                          ServiceStorage serviceStorage, RevenueStorage revenueStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.serviceStorage = serviceStorage;
        this.revenueStorage = revenueStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }

    // ================ ServiceManager methods ==============================

    @Override
    public Path getServiceManagerStorageFilePath() {
        return serviceStorage.getServiceManagerStorageFilePath();
    }

    @Override
    public Optional<ReadOnlyServiceManager> readServiceManager() throws DataConversionException, IOException {
        return readServiceManager(serviceStorage.getServiceManagerStorageFilePath());
    }

    @Override
    public Optional<ReadOnlyServiceManager> readServiceManager(Path filePath) throws DataConversionException,
            IOException {
        return serviceStorage.readServiceManager(filePath);
    }

    @Override
    public void saveServiceManager(ReadOnlyServiceManager serviceManager) throws IOException {
        saveServiceManager(serviceManager, serviceStorage.getServiceManagerStorageFilePath());
    }

    @Override
    public void saveServiceManager(ReadOnlyServiceManager serviceManager, Path filePath) throws IOException {
        serviceStorage.saveServiceManager(serviceManager, filePath);
    }

    // ================ RevenueTracker methods ==============================

    @Override
    public Path getRevenueTrackerStorageFilePath() {
        return revenueStorage.getRevenueTrackerStorageFilePath();
    }

    @Override
    public Optional<ReadOnlyRevenueTracker> readRevenueTracker() throws DataConversionException, IOException {
        return readRevenueTracker(revenueStorage.getRevenueTrackerStorageFilePath());
    }

    @Override
    public Optional<ReadOnlyRevenueTracker> readRevenueTracker(Path filePath) throws DataConversionException,
            IOException {
        return revenueStorage.readRevenueTracker(filePath);
    }

    @Override
    public void saveRevenueTracker(ReadOnlyRevenueTracker revenueTracker) throws IOException {
        saveRevenueTracker(revenueTracker, revenueStorage.getRevenueTrackerStorageFilePath());
    }

    @Override
    public void saveRevenueTracker(ReadOnlyRevenueTracker revenueTracker, Path filePath) throws IOException {
        revenueStorage.saveRevenueTracker(revenueTracker, filePath);
    }
}
