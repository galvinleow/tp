package seedu.address.logic.parser.expense;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISFIXED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.expense.EditExpenseCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditExpenseCommand object
 */
public class EditExpenseCommandParser implements Parser<EditExpenseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of
     * the EditExpenseCommand and returns an EditExpenseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditExpenseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION, PREFIX_ISFIXED,
                        PREFIX_AMOUNT, PREFIX_DATE, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditExpenseCommand.MESSAGE_USAGE), pe);
        }

        EditExpenseCommand.EditExpenseDescriptor editExpenseDescriptor = new EditExpenseCommand.EditExpenseDescriptor();
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            editExpenseDescriptor.setDescription(ParserUtil.parseDescription(
                    argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        if (argMultimap.getValue(PREFIX_ISFIXED).isPresent()) {
            editExpenseDescriptor.setIsFixed(ParserUtil.parseIsFixed(argMultimap.getValue(PREFIX_ISFIXED).get()));
        }
        if (argMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
            editExpenseDescriptor.setAmount(ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get()));
        }
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            editExpenseDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            editExpenseDescriptor.setTag(ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get()));
        }
        if (!editExpenseDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditExpenseCommand.MESSAGE_NOT_EDITED);
        }

        return new EditExpenseCommand(index, editExpenseDescriptor);
    }
}

