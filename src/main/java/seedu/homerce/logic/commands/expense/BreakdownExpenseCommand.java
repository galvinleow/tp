package seedu.homerce.logic.commands.expense;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_MONTH_OF_YEAR;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_YEAR;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.homerce.logic.commands.Command;
import seedu.homerce.logic.commands.CommandResult;
import seedu.homerce.model.Model;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.expense.predicate.ExpenseMonthPredicate;
import seedu.homerce.model.expense.predicate.ExpenseYearPredicate;

public class BreakdownExpenseCommand extends Command {

    public static final String COMMAND_WORD = "breakdownexp";
    public static final String MESSAGE_SUCCESS = "Expenses breakdown: %1$s";
    public static final String MESSAGE_USAGE = "Displays the breakdown of expenses for the month "
            + "and year specified \n"
            + "Parameters: "
            + PREFIX_MONTH_OF_YEAR + "MONTH "
            + PREFIX_YEAR + "YEAR \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MONTH_OF_YEAR + "12"
            + PREFIX_YEAR + "2020";

    private final Month month;
    private final Year year;

    /**
     * Breaks down expenses by Tag Name.
     */
    public BreakdownExpenseCommand(Month month, Year year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Expense> filteredExpenseList = createFilteredExpense(model);
        Map<String, Double> expenseMap = breakdownExpenses(filteredExpenseList);
        String expenseMapString = expenseMapToString(expenseMap);
        return new CommandResult(String.format(MESSAGE_SUCCESS, expenseMapString));
    }

    /**
     * Creates a Hashmap where key = Tag Name and value = Total Expense Amount per Tag.
     */
    private Map<String, Double> breakdownExpenses(List<Expense> expenseList) {
        requireNonNull(expenseList);
        if (expenseList.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Double> expenseMap = new HashMap<>();
        int listSize = expenseList.size();
        for (int i = 0; i < listSize; i++) {
            Expense expense = expenseList.get(i);
            String tagName = expense.getTag().tagName;
            Double expenseAmount = expense.getValue().value.doubleValue();
            if (!expenseMap.containsKey(tagName)) {
                expenseMap.put(tagName, expenseAmount);
            } else {
                Double originalAmount = expenseMap.get(tagName);
                Double newAmount = originalAmount + expenseAmount;
                expenseMap.replace(tagName, newAmount);
            }
        }
        return expenseMap;

    }

    /**
     * Creates a list of expenses filtered by the year and month specified by the user
     */
    private List<Expense> createFilteredExpense(Model model) {
        Predicate<Expense> expenseMonthPredicate = new ExpenseMonthPredicate(month);
        Predicate<Expense> expenseYearPredicate = new ExpenseYearPredicate(year);
        List<Expense> yearFilteredExpense = model.filterExpenseByYear(expenseYearPredicate);
        List<Expense> filteredExpense = yearFilteredExpense.stream()
                .filter(x -> expenseMonthPredicate.test(x)).collect(Collectors.toList());

        return filteredExpense;
    }

    /**
     * Converts an expenseMap to a String and calculates the percentage of each Tag's total expense amount.
     */
    private String expenseMapToString(Map<String, Double> expenseMap) {
        if (expenseMap.isEmpty()) {
            return "\n No expenses for the month";
        }
        double totalExpenseAmount = 0;
        for (String tagName : expenseMap.keySet()) {
            totalExpenseAmount += expenseMap.get(tagName);
        }
        String expenseMapString = "\n";
        for (String tagName : expenseMap.keySet()) {
            double tagPercentage = (expenseMap.get(tagName) / totalExpenseAmount) * 100;
            double roundedTagPercentage = Math.round(tagPercentage * 10) / 10.0;
            expenseMapString += "[" + tagName + ": $" + expenseMap.get(tagName) + ", "
                    + roundedTagPercentage + "% ] \n";
        }
        return expenseMapString;
    }

}
