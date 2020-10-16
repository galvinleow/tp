package seedu.homerce.logic.commands.service;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_DURATION;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_PRICE;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_SERVICE_TITLE;

import java.util.List;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.service.Service;
import seedu.homerce.model.util.attributes.service.ServiceCodeGenerator;

/**
 * Adds a service to SuperSalon.
 */
public class AddServiceCommand extends Command {

    public static final String COMMAND_WORD = "addsvc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a service to the list of services "
            + "Parameters: "
            + PREFIX_SERVICE_TITLE + "TITLE "
            + PREFIX_SERVICE_DURATION + "DURATION "
            + PREFIX_SERVICE_PRICE + "PRICE "
            + " Example: " + COMMAND_WORD + " "
            + PREFIX_SERVICE_TITLE + "Lash Lift"
            + PREFIX_SERVICE_DURATION + "0.5 "
            + PREFIX_SERVICE_PRICE + "38 ";

    private static final String MESSAGE_SUCCESS = "New Service added: %1$s";
    private final Service toAdd;

    /**
     * Adds a Service object as an attribute for the AddServiceCommand object.
     * @param toAdd is the Service to be added.
     */
    public AddServiceCommand(Service toAdd) {
        requireNonNull(toAdd);
        this.toAdd = toAdd;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);

        // Generate unique ServiceCode for the Service before adding it to model
        List<Service> lastShownList = model.getFilteredServiceList();
        String serviceCode = ServiceCodeGenerator.generateNewServiceCode(lastShownList);
        toAdd.addServiceCode(serviceCode);

        model.addService(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
