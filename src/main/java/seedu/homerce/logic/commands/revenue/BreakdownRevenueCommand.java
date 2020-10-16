package seedu.homerce.logic.commands.revenue;

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
import seedu.homerce.model.HistoryManager;
import seedu.homerce.model.Model;
import seedu.homerce.model.revenue.Revenue;
import seedu.homerce.model.revenue.predicate.RevenueMonthPredicate;
import seedu.homerce.model.revenue.predicate.RevenueYearPredicate;

/**
 * Breaks down revenue by Service.
 */
public class BreakdownRevenueCommand extends Command {

    public static final String COMMAND_WORD = "breakdownrev";
    public static final String MESSAGE_SUCCESS = "Revenue breakdown: %1$s";
    public static final String MESSAGE_USAGE = "Displays the breakdown of revenue for the month and year specified \n"
            + "Parameters: " + PREFIX_MONTH_OF_YEAR + "MONTH " + PREFIX_YEAR + "YEAR \n" + "Example: " + COMMAND_WORD
            + " " + PREFIX_MONTH_OF_YEAR + "12" + PREFIX_YEAR + "2020";

    private final Month month;
    private final Year year;

    /**
     * Constructor for breakdown of revenue.
     *
     * @param month month of revenue, value from 1-12
     * @param year year of revenue, value > 0
     */
    public BreakdownRevenueCommand(Month month, Year year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public CommandResult execute(Model model, HistoryManager historyManager) {
        requireNonNull(model);
        List<Revenue> filteredRevenueList = createFilteredRevenue(model);
        Map<String, Double> revenueMap = breakdownRevenues(filteredRevenueList);
        String revenueMapString = revenueMapToString(revenueMap);
        return new CommandResult(String.format(MESSAGE_SUCCESS, revenueMapString));
    }

    /**
     * Creates a Hashmap where key = Service Title and value = Total Revenue Amount per Service.
     */
    private Map<String, Double> breakdownRevenues(List<Revenue> revenueList) {
        requireNonNull(revenueList);
        if (revenueList.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Double> revenueMap = new HashMap<>();
        for (Revenue revenue : revenueList) {
            String revenueTitle = revenue.getService().getTitle().value;
            double revenueAmount = revenue.getValue().value.doubleValue();
            if (revenueMap.containsKey(revenueTitle)) {
                double current = revenueMap.get(revenueTitle);
                revenueMap.replace(revenueTitle, current + revenueAmount);
            } else {
                revenueMap.put(revenueTitle, revenueAmount);
            }
        }
        return revenueMap;
    }

    /**
     * Creates a list of revenue filtered by the year and month specified by the user
     */
    private List<Revenue> createFilteredRevenue(Model model) {
        Predicate<Revenue> revenueMonthPredicate = new RevenueMonthPredicate(month);
        Predicate<Revenue> revenueYearPredicate = new RevenueYearPredicate(year);
        List<Revenue> yearFilteredRevenue = model.filterRevenueByYear(revenueYearPredicate);
        return yearFilteredRevenue.stream().filter(revenueMonthPredicate).collect(Collectors.toList());
    }

    /**
     * Converts an revenueMap to a String and calculates the percentage of each Service total revenue amount.
     */
    private String revenueMapToString(Map<String, Double> revenueMap) {
        if (revenueMap.isEmpty()) {
            return "\n No revenue for the month";
        }
        double totalRevenue = 0;
        for (Double value : revenueMap.values()) {
            totalRevenue += value;
        }

        StringBuilder revenueString = new StringBuilder("\n");
        for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {
            double percentage = (entry.getValue() / totalRevenue) * 100;
            revenueString.append(String.format("[%s] : $%.2f, %.2f%%\n", entry.getKey(), entry.getValue(), percentage));
        }
        revenueString.append("Total Revenue : $").append(totalRevenue);

        return revenueString.toString();
    }

}
