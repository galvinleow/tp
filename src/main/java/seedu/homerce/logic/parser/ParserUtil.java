package seedu.homerce.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.homerce.commons.core.index.Index;
import seedu.homerce.commons.util.StringUtil;
import seedu.homerce.logic.parser.exceptions.ParseException;
import seedu.homerce.model.appointment.TimeOfDay;
import seedu.homerce.model.client.Email;
import seedu.homerce.model.client.Name;
import seedu.homerce.model.client.Phone;
import seedu.homerce.model.expense.IsFixed;
import seedu.homerce.model.service.Duration;
import seedu.homerce.model.service.ServiceCode;
import seedu.homerce.model.util.attributes.Amount;
import seedu.homerce.model.util.attributes.Date;
import seedu.homerce.model.util.attributes.Description;
import seedu.homerce.model.util.attributes.Tag;
import seedu.homerce.model.util.attributes.Title;


/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_DAY_FORMAT = "Day should contain only the first 3 letters of the day.";
    public static final String MESSAGE_INVALID_DURATION = "Duration should be a number in hours.";
    public static final String MESSAGE_INVALID_MONTH = "Month should be a number between 1 - 12";
    public static final String MESSAGE_INVALID_YEAR = "Year should be a positive valid number";
    public static final int VALID_YEAR = 2020;
    public static final int VALID_MONTH = 12;

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses {@code String date} into a {@code Date}.
     */
    public static Date parseDate(String date) throws ParseException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!Date.isValidDate(date)) {
            throw new ParseException(Date.MESSAGE_CONSTRAINTS);
        }
        return new Date(trimmedDate);
    }

    /**
     * Parses {@code String description} into a {@code Description}.
     */
    public static Description parseDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!Description.isValidDescription(description)) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }
        return new Description(trimmedDescription);
    }

    /**
     * Parses {@code String isFixed} into a {@code IsFixed}.
     */
    public static IsFixed parseIsFixed(String isFixed) throws ParseException {
        requireNonNull(isFixed);
        if (!IsFixed.isValidIsFixed(isFixed)) {
            throw new ParseException(IsFixed.MESSAGE_CONSTRAINTS);
        }
        return new IsFixed(isFixed);
    }

    /**
     * Parses a {@code String title} into an {@code Title}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code title} is invalid.
     */
    public static Title parseTitle(String title) throws ParseException {
        requireNonNull(title);
        String trimmedTitle = title.trim();
        if (!Title.isValidTitle(trimmedTitle)) {
            throw new ParseException(Title.MESSAGE_CONSTRAINTS);
        }
        return new Title(trimmedTitle);
    }

    /**
     * Parses a {@code String Amount} into a {@code {Amount}}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code amount} is invalid.
     */
    public static Amount parseAmount(String amount) throws ParseException {
        requireNonNull(amount);
        amount = amount.trim();
        try {
            Double doubleAmount = Double.parseDouble(amount);
            if (!Amount.isValidAmount(doubleAmount)) {
                throw new ParseException(Amount.MESSAGE_CONSTRAINTS);
            }
            return new Amount(doubleAmount);
        } catch (NumberFormatException e) {
            throw new ParseException(Amount.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String Duration} into a {@code {Duration}}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code duration} is invalid.
     */
    public static Duration parseDuration(String duration) throws ParseException {
        requireNonNull(duration);
        duration = duration.trim();
        try {
            Double doubleDuration = Double.parseDouble(duration);
            if (!Duration.isValidDuration(doubleDuration)) {
                throw new ParseException(Duration.MESSAGE_CONSTRAINTS);
            }
            return new Duration(doubleDuration);
        } catch (NumberFormatException e) {
            throw new ParseException(Duration.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String serviceCode} into a {@code {ServiceCode}}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code serviceCode} is invalid.
     */
    public static ServiceCode parseServiceCode(String serviceCode) throws ParseException {
        requireNonNull(serviceCode);
        String trimmedServiceCode = serviceCode.trim();
        if (!ServiceCode.isValidServiceCode(trimmedServiceCode)) {
            throw new ParseException(ServiceCode.MESSAGE_CONSTRAINTS);
        }
        return new ServiceCode(trimmedServiceCode);
    }

    /**
     * Parses a {@code String timeOfDay} into a {@code {TimeOfDay}}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code timeOfDay} is invalid.
     */
    public static TimeOfDay parseTime(String timeOfDay) throws ParseException {
        requireNonNull(timeOfDay);
        String trimmedTimeOfDay = timeOfDay.trim();
        if (!TimeOfDay.isValidTime(trimmedTimeOfDay)) {
            throw new ParseException(TimeOfDay.MESSAGE_CONSTRAINTS);
        }
        return new TimeOfDay(trimmedTimeOfDay);
    }

    /**
     * Parses a {@code String month} into a {@code {Month}}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code month} is invalid.
     */
    public static Month parseMonth(String month) throws ParseException {
        requireNonNull(month);
        String trimmedMonth = month.trim();
        if (!isValidMonth(trimmedMonth)) {
            throw new ParseException(MESSAGE_INVALID_MONTH);
        }
        return Month.of(Integer.parseInt(trimmedMonth));
    }

    /**
     * Parses a {@code String year} into a {@code {Year}}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code year} is invalid.
     */
    public static Year parseYear(String year) throws ParseException {
        requireNonNull(year);
        String trimmedYear = year.trim();
        if (!isValidYear(trimmedYear)) {
            throw new ParseException(MESSAGE_INVALID_YEAR);
        }
        return Year.of(Integer.parseInt(trimmedYear));
    }

    private static boolean isValidYear(String year) {
        // Checks if its a String contains numbers only
        if (!year.matches("\\d+")) {
            return false;
        }
        int intYear = Integer.parseInt(year);
        Year yearObject = Year.of(VALID_YEAR);
        ValueRange range = yearObject.range(ChronoField.YEAR);
        return range.isValidIntValue(intYear);
    }

    private static boolean isValidMonth(String month) {
        // Checks if its a String contains numbers only
        if (!month.matches("\\d+")) {
            return false;
        }
        int intMonth = Integer.parseInt(month);
        Month monthObject = Month.of(VALID_MONTH);
        ValueRange range = monthObject.range(ChronoField.MONTH_OF_YEAR);
        return range.isValidIntValue(intMonth);
    }
}

