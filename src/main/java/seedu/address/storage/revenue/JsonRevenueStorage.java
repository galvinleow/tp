package seedu.address.storage.revenue;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.manager.ReadOnlyRevenueTracker;
import seedu.address.storage.service.JsonServiceStorage;

/**
 * A class to access RevenueTracker data stored as a json file on the hard disk.
 */
public class JsonRevenueStorage implements RevenueStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonServiceStorage.class);
    private Path filePath;

    public JsonRevenueStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getRevenueTrackerStorageFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyRevenueTracker> readRevenueTracker() throws DataConversionException, IOException {
        return Optional.empty();
    }

    /**
     * Similar to {@link #readRevenueTracker()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    @Override
    public Optional<ReadOnlyRevenueTracker> readRevenueTracker(Path filePath) throws DataConversionException,
            IOException {
        requireNonNull(filePath);

        Optional<JsonSerializableRevenueTracker> jsonRevenueTracker = JsonUtil.readJsonFile(
                filePath, JsonSerializableRevenueTracker.class);
        if (jsonRevenueTracker.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonRevenueTracker.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveRevenueTracker(ReadOnlyRevenueTracker revenueTracker) throws IOException {
        saveRevenueTracker(revenueTracker, filePath);
    }

    @Override
    public void saveRevenueTracker(ReadOnlyRevenueTracker revenueTracker, Path filePath) throws IOException {
        requireNonNull(revenueTracker);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableRevenueTracker(revenueTracker), filePath);
    }
}
