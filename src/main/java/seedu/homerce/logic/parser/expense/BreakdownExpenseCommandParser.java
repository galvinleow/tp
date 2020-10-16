package seedu.homerce.logic.parser.expense;


import static seedu.homerce.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_MONTH_OF_YEAR;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_YEAR;

import java.time.Month;
import java.time.Year;
import java.util.stream.Stream;

import seedu.homerce.logic.commands.expense.BreakdownExpenseCommand;
import seedu.homerce.logic.parser.ArgumentMultimap;
import seedu.homerce.logic.parser.ArgumentTokenizer;
import seedu.homerce.logic.parser.Parser;
import seedu.homerce.logic.parser.ParserUtil;
import seedu.homerce.logic.parser.Prefix;
import seedu.homerce.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ProfitCommand object
 */
public class BreakdownExpenseCommandParser implements Parser<BreakdownExpenseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the BreakdownExpenseCommand
     * and returns a BreakdownExpenseCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public BreakdownExpenseCommand parse(String args) throws ParseException {
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MONTH_OF_YEAR, PREFIX_YEAR);

        if (!arePrefixesPresent(argumentMultimap, PREFIX_MONTH_OF_YEAR, PREFIX_YEAR)
                || !argumentMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    BreakdownExpenseCommand.MESSAGE_USAGE));
        }

        Month month = ParserUtil.parseMonth(argumentMultimap.getValue(PREFIX_MONTH_OF_YEAR).get());
        Year year = ParserUtil.parseYear(argumentMultimap.getValue(PREFIX_YEAR).get());

        return new BreakdownExpenseCommand(month, year);
    }

    /**
     * * Returns true if none of the prefixes contains empty {@code Optional} values
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
