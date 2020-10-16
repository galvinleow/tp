package seedu.homerce.logic.parser.expense;

import static seedu.homerce.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_ISFIXED;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.stream.Stream;

import seedu.homerce.logic.commands.expense.AddExpenseCommand;
import seedu.homerce.logic.parser.ArgumentMultimap;
import seedu.homerce.logic.parser.ArgumentTokenizer;
import seedu.homerce.logic.parser.Parser;
import seedu.homerce.logic.parser.ParserUtil;
import seedu.homerce.logic.parser.Prefix;
import seedu.homerce.logic.parser.exceptions.ParseException;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.expense.IsFixed;
import seedu.homerce.model.util.attributes.Amount;
import seedu.homerce.model.util.attributes.Date;
import seedu.homerce.model.util.attributes.Description;
import seedu.homerce.model.util.attributes.Tag;

/**
 * Parses input arguments and creates a new AddExpenseCommand object
 */
public class AddExpenseCommandParser implements Parser<AddExpenseCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddExpenseCommand
     * and returns an AddExpenseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddExpenseCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION, PREFIX_ISFIXED, PREFIX_AMOUNT,
                        PREFIX_DATE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_DESCRIPTION, PREFIX_ISFIXED, PREFIX_AMOUNT, PREFIX_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddExpenseCommand.MESSAGE_USAGE));
        }

        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        IsFixed isFixed = ParserUtil.parseIsFixed(argMultimap.getValue(PREFIX_ISFIXED).get());
        Amount value = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get());
        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        Tag tag;
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());
        } else {
            tag = new Tag();
        }

        Expense expense = new Expense(description, isFixed, value, date, tag);

        return new AddExpenseCommand(expense);
    }

    /**
     * * Returns true if none of the prefixes contains empty {@code Optional} values
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
