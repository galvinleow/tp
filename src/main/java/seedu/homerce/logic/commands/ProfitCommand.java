package seedu.homerce.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_MONTH_OF_YEAR;
import static seedu.homerce.logic.parser.CliSyntax.PREFIX_YEAR;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.homerce.logic.commands.exceptions.CommandException;
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.expense.Expense;
import seedu.homerce.model.expense.predicate.ExpenseMonthPredicate;
import seedu.homerce.model.expense.predicate.ExpenseYearPredicate;
import seedu.homerce.model.revenue.Revenue;
import seedu.homerce.model.revenue.predicate.RevenueMonthPredicate;
import seedu.homerce.model.revenue.predicate.RevenueYearPredicate;

public class ProfitCommand extends Command {

    public static final String COMMAND_WORD = "profit";

    public static final String MESSAGE_SUCCESS = "The profit for this month is %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays the profit for the month and year specified "
            + "Parameters: "
            + PREFIX_MONTH_OF_YEAR + "MONTH "
            + PREFIX_YEAR + "YEAR "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MONTH_OF_YEAR + "12 "
            + PREFIX_YEAR + "2020";

    private Month month;
    private Year year;

    /**
     * Displays the profit generated for the month and year specified
     */
    public ProfitCommand(Month month, Year year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) throws CommandException {
        requireNonNull(model);

        List<Expense> filteredExpense = createFilteredExpense(model);
        List<Revenue> filteredRevenue = createFilteredRevenue(model);

        String profit = getProfit(filteredExpense, filteredRevenue);
        return new CommandResult(String.format(MESSAGE_SUCCESS, profit));
    }

    private String getProfit(List<Expense> expenses, List<Revenue> revenues) {
        BigDecimal totalExpense = expenses.stream()
                .map(x -> x.getValue().value).reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal totalRevenue = revenues.stream()
                .map(x -> x.getValue().value).reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal profit = totalRevenue.subtract(totalExpense);

        return profit.toString();
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
     * Creates a list of revenue filtered by the year and month specified by the user
     */
    private List<Revenue> createFilteredRevenue(Model model) {
        Predicate<Revenue> revenueMonthPredicate = new RevenueMonthPredicate(month);
        Predicate<Revenue> revenueYearPredicate = new RevenueYearPredicate(year);
        List<Revenue> yearFilteredRevenue = model.filterRevenueByYear(revenueYearPredicate);
        List<Revenue> filteredRevenue = yearFilteredRevenue.stream()
                .filter(x -> revenueMonthPredicate.test(x)).collect(Collectors.toList());

        return filteredRevenue;
    }

}
