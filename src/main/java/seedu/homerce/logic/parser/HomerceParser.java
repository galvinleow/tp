package seedu.homerce.logic.parser;

import static seedu.homerce.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.homerce.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.ExitCommand;
import seedu.homerce.logic.commands.HelpCommand;
import seedu.homerce.logic.commands.ProfitCommand;
import seedu.homerce.logic.commands.UndoCommand;
import seedu.homerce.logic.commands.appointment.AddAppointmentCommand;
import seedu.homerce.logic.commands.appointment.ClearAppointmentCommand;
import seedu.homerce.logic.commands.appointment.DeleteAppointmentCommand;
import seedu.homerce.logic.commands.appointment.DoneAppointmentCommand;
import seedu.homerce.logic.commands.appointment.EditAppointmentCommand;
import seedu.homerce.logic.commands.appointment.FindAppointmentCommand;
import seedu.homerce.logic.commands.appointment.ListAppointmentCommand;
import seedu.homerce.logic.commands.appointment.UnDoneAppointmentCommand;
import seedu.homerce.logic.commands.client.AddClientCommand;
import seedu.homerce.logic.commands.client.ClearClientCommand;
import seedu.homerce.logic.commands.client.DeleteClientCommand;
import seedu.homerce.logic.commands.client.EditClientCommand;
import seedu.homerce.logic.commands.client.FindClientCommand;
import seedu.homerce.logic.commands.client.ListClientCommand;
import seedu.homerce.logic.commands.expense.AddExpenseCommand;
import seedu.homerce.logic.commands.expense.BreakdownExpenseCommand;
import seedu.homerce.logic.commands.expense.ClearExpenseCommand;
import seedu.homerce.logic.commands.expense.DeleteExpenseCommand;
import seedu.homerce.logic.commands.expense.EditExpenseCommand;
import seedu.homerce.logic.commands.expense.FindExpenseCommand;
import seedu.homerce.logic.commands.expense.ListExpenseCommand;
import seedu.homerce.logic.commands.revenue.ClearRevenueCommand;
import seedu.homerce.logic.commands.revenue.FindRevenueCommand;
import seedu.homerce.logic.commands.revenue.ListRevenueCommand;
import seedu.homerce.logic.commands.service.AddServiceCommand;
import seedu.homerce.logic.commands.service.ClearServiceCommand;
import seedu.homerce.logic.commands.service.DeleteServiceCommand;
import seedu.homerce.logic.commands.service.EditServiceCommand;
import seedu.homerce.logic.commands.service.FindServiceCommand;
import seedu.homerce.logic.commands.service.ListServiceCommand;
import seedu.homerce.logic.parser.appointment.AddAppointmentCommandParser;
import seedu.homerce.logic.parser.appointment.DeleteAppointmentCommandParser;
import seedu.homerce.logic.parser.appointment.DoneAppointmentCommandParser;
import seedu.homerce.logic.parser.appointment.EditAppointmentCommandParser;
import seedu.homerce.logic.parser.appointment.FindAppointmentCommandParser;
import seedu.homerce.logic.parser.appointment.UnDoneAppointmentCommandParser;
import seedu.homerce.logic.parser.client.AddClientCommandParser;
import seedu.homerce.logic.parser.client.DeleteClientCommandParser;
import seedu.homerce.logic.parser.client.EditClientCommandParser;
import seedu.homerce.logic.parser.client.FindClientCommandParser;
import seedu.homerce.logic.parser.exceptions.ParseException;
import seedu.homerce.logic.parser.expense.AddExpenseCommandParser;
import seedu.homerce.logic.parser.expense.BreakdownExpenseCommandParser;
import seedu.homerce.logic.parser.expense.DeleteExpenseCommandParser;
import seedu.homerce.logic.parser.expense.EditExpenseCommandParser;
import seedu.homerce.logic.parser.expense.FindExpenseCommandParser;
import seedu.homerce.logic.parser.revenue.FindRevenueCommandParser;
import seedu.homerce.logic.parser.service.AddServiceCommandParser;
import seedu.homerce.logic.parser.service.DeleteServiceCommandParser;
import seedu.homerce.logic.parser.service.EditServiceCommandParser;
import seedu.homerce.logic.parser.service.FindServiceCommandParser;


/**
 * Parses user input.
 */
public class HomerceParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddClientCommand.COMMAND_WORD:
            return new AddClientCommandParser().parse(arguments);

        case EditClientCommand.COMMAND_WORD:
            return new EditClientCommandParser().parse(arguments);

        case DeleteClientCommand.COMMAND_WORD:
            return new DeleteClientCommandParser().parse(arguments);

        case ClearClientCommand.COMMAND_WORD:
            return new ClearClientCommand();

        case FindClientCommand.COMMAND_WORD:
            return new FindClientCommandParser().parse(arguments);

        case ListClientCommand.COMMAND_WORD:
            return new ListClientCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddExpenseCommand.COMMAND_WORD:
            return new AddExpenseCommandParser().parse(arguments);

        case EditExpenseCommand.COMMAND_WORD:
            return new EditExpenseCommandParser().parse(arguments);

        case FindExpenseCommand.COMMAND_WORD:
            return new FindExpenseCommandParser().parse(arguments);

        case DeleteExpenseCommand.COMMAND_WORD:
            return new DeleteExpenseCommandParser().parse(arguments);

        case ListExpenseCommand.COMMAND_WORD:
            return new ListExpenseCommand();

        case ClearExpenseCommand.COMMAND_WORD:
            return new ClearExpenseCommand();

        case BreakdownExpenseCommand.COMMAND_WORD:
            return new BreakdownExpenseCommandParser().parse(arguments);

        case AddServiceCommand.COMMAND_WORD:
            return new AddServiceCommandParser().parse(arguments);

        case FindServiceCommand.COMMAND_WORD:
            return new FindServiceCommandParser().parse(arguments);

        case ClearServiceCommand.COMMAND_WORD:
            return new ClearServiceCommand();

        case ListServiceCommand.COMMAND_WORD:
            return new ListServiceCommand();

        case DeleteServiceCommand.COMMAND_WORD:
            return new DeleteServiceCommandParser().parse(arguments);

        case EditServiceCommand.COMMAND_WORD:
            return new EditServiceCommandParser().parse(arguments);

        case ListRevenueCommand.COMMAND_WORD:
            return new ListRevenueCommand();

        case FindRevenueCommand.COMMAND_WORD:
            return new FindRevenueCommandParser().parse(arguments);

        case ClearRevenueCommand.COMMAND_WORD:
            return new ClearRevenueCommand();

        case AddAppointmentCommand.COMMAND_WORD:
            return new AddAppointmentCommandParser().parse(arguments);

        case ListAppointmentCommand.COMMAND_WORD:
            return new ListAppointmentCommand();

        case FindAppointmentCommand.COMMAND_WORD:
            return new FindAppointmentCommandParser().parse(arguments);

        case DeleteAppointmentCommand.COMMAND_WORD:
            return new DeleteAppointmentCommandParser().parse(arguments);

        case ClearAppointmentCommand.COMMAND_WORD:
            return new ClearAppointmentCommand();

        case DoneAppointmentCommand.COMMAND_WORD:
            return new DoneAppointmentCommandParser().parse(arguments);

        case UnDoneAppointmentCommand.COMMAND_WORD:
            return new UnDoneAppointmentCommandParser().parse(arguments);

        case EditAppointmentCommand.COMMAND_WORD:
            return new EditAppointmentCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case ProfitCommand.COMMAND_WORD:
            return new ProfitCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
