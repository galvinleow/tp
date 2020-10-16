package seedu.homerce.model.service;

import static java.util.Objects.requireNonNull;
import static seedu.homerce.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import seedu.homerce.model.service.exceptions.ServiceNotFoundException;
import seedu.homerce.model.util.uniquelist.UniqueList;

public class UniqueServiceList extends UniqueList<Service> {

    /**
     * Returns true if the list contains an equivalent item as the given argument.
     */
    public boolean contains(ServiceCode toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(x -> x.getServiceCode().equals(toCheck));
    }

    /**
     * Find the service based on service code. If service code does not match a service, exception is thrown.
     */
    public Service getServiceByServiceCode(ServiceCode serviceCode) {
        requireAllNonNull(serviceCode);
        for (Service service : internalList) {
            if (service.getServiceCode().equals(serviceCode)) {
                return service;
            }
        }
        throw new ServiceNotFoundException();
    }

    /**
     * Creates a deep copy of all the services in the unique list of services.
     *
     * @return a list of services.
     */
    public List<Service> deepCopy() {
        Iterator<Service> iterator = this.iterator();
        List<Service> servicesCopy = new ArrayList<>();
        while (iterator.hasNext()) {
            servicesCopy.add(iterator.next());
        }
        return servicesCopy;
    }
}
