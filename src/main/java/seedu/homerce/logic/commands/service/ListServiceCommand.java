package seedu.homerce.logic.commands.service;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.model.Model.PREDICATE_SHOW_ALL_SERVICES;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;

/**
 * Lists all the services in Homerce to the user.
 */
public class ListServiceCommand extends Command {

    public static final String COMMAND_WORD = "listsvc";

    public static final String MESSAGE_SUCCESS = "Listed all services";


    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        model.updateFilteredServiceList(PREDICATE_SHOW_ALL_SERVICES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
