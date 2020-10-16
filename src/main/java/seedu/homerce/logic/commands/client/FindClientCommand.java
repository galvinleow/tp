package seedu.homerce.logic.commands.client;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.function.Predicate;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.client.Client;

/**
 * Finds and lists all clients in homerce book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindClientCommand extends Command {

    public static final String COMMAND_WORD = "findcli";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all clients by either their name or phone "
         + "(case-sensitive) and displays them as a list with index numbers.\n"
             + "Parameters: "
             + PREFIX_NAME + "NAME or "
        + PREFIX_PHONE + "PHONE "
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_NAME + "John";

    private final Predicate<Client> predicate;

    public FindClientCommand(Predicate<Client> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredClientList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW, model.getFilteredClientList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindClientCommand // instanceof handles nulls
                && predicate.equals(((FindClientCommand) other).predicate)); // state check
    }
}
