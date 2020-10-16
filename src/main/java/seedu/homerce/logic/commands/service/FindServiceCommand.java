package seedu.homerce.logic.commands.service;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_SERVICE_CODE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_TITLE;

import java.util.function.Predicate;

import seedu.homerce.commons.core.Messages;
import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.service.Service;

/**
 * Finds and lists all services in Homerce whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindServiceCommand extends Command {
    public static final String COMMAND_WORD = "findsvc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all services by either their "
        + "title or service code (case-sensitive) and displays them as a list with index numbers.\n"
        + "Parameters: "
        + PREFIX_SERVICE_TITLE + "TITLE or "
        + PREFIX_SERVICE_SERVICE_CODE + "SERVICE_CODE "
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_SERVICE_TITLE + "Lash Lift";

    private final Predicate<Service> predicate;

    public FindServiceCommand(Predicate<Service> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredServiceList(predicate);
        return new CommandResult(
            String.format(Messages.MESSAGE_SERVICES_LISTED_OVERVIEW, model.getFilteredServiceList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof FindServiceCommand// instanceof handles nulls
            && predicate.equals(((FindServiceCommand) other).predicate)); // state check
    }
}
