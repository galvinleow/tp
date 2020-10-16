package seedu.homerce.model;

import java.util.LinkedList;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.HelpCommand;
import seedu.homerce.logic.commands.UndoCommand;
import seedu.homerce.logic.commands.appointment.FindAppointmentCommand;
import seedu.homerce.logic.commands.appointment.ListAppointmentCommand;
import seedu.homerce.logic.commands.client.FindClientCommand;
import seedu.homerce.logic.commands.client.ListClientCommand;
import seedu.homerce.logic.commands.expense.FindExpenseCommand;
import seedu.homerce.logic.commands.expense.ListExpenseCommand;
import seedu.homerce.logic.commands.revenue.FindRevenueCommand;
import seedu.homerce.logic.commands.revenue.ListRevenueCommand;
import seedu.homerce.logic.commands.service.FindServiceCommand;
import seedu.homerce.logic.commands.service.ListServiceCommand;


/**
 * Holds all the previous states of Homerce's storage.
 *
 * A new HistoryManager is initialized upon each start up of Homerce.
 */
public class HistoryManager {
    private LinkedList<Model> models;

    public HistoryManager() {
        this.models = new LinkedList<>();
    }

    /**
     * Adds a model to the history of model states.
     * The model input will not be added if the command given does not change the state of the model.
     *
     * @param model the new model to be added to the history of model states.
     * @param command the latest command given by the user.
     */
    public void addToHistory(Model model, Command command) {
        Model latestModelState = models.peekLast();
        if (willCommandChangeState(command)) {
            Model modelDeepCopy = model.deepCopy();
            models.addLast(modelDeepCopy);
        }
    }

    /**
     * Checks if the user command will change the storage state of Homerce.
     *
     * @param command the command given by the user.
     * @return true if the command will change the state of Homerce.
     */
    private static boolean willCommandChangeState(Command command) {
        return !(command instanceof UndoCommand) && !(command instanceof HelpCommand)
            && !(command instanceof FindClientCommand) && !(command instanceof ListClientCommand)
            && !(command instanceof FindExpenseCommand) && !(command instanceof ListExpenseCommand)
            && !(command instanceof FindServiceCommand) && !(command instanceof ListServiceCommand)
            && !(command instanceof FindRevenueCommand) && !(command instanceof ListRevenueCommand)
            && !(command instanceof FindAppointmentCommand) && !(command instanceof ListAppointmentCommand);
    }

    public Model getPreviousState() {
        return models.pollLast();
    }
}
