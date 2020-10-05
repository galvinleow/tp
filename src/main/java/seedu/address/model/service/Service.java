package seedu.address.model.service;

import seedu.address.model.util.attributes.Amount;
import seedu.address.model.util.attributes.Description;

public class Service {
    /** The name of the service */
    private ServiceTitle title;

    /** Amount of money in dollars that this Service brings in */
    private Amount price;

    /** Unique identification number for each Service */
    private ServiceCode serviceCode;

    /** How long the service takes */
    private Duration duration;

    /**
     * Constructor for Service.
     * @param title The title of the service.
     * @param price The price of the service.
     * @param duration The duration of the service.
     */
    public Service(ServiceTitle title, Amount price, Duration duration) {
        this.title = title;
        this.price = price;
        this.duration = duration;
    }
}
