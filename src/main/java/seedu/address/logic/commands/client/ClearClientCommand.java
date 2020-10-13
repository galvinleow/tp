package seedu.address.logic.commands.client;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.manager.ClientManager;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearClientCommand extends Command {

    public static final String COMMAND_WORD = "clearcli";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new ClientManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
