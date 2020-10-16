package seedu.homerce.model.manager;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.homerce.model.client.Client;
import seedu.homerce.model.client.Phone;
import seedu.homerce.model.client.UniqueClientList;

/**
 * Wraps all data at the homerce-book level
 * Duplicates are not allowed (by .isSameClient comparison)
 */
public class ClientManager implements ReadOnlyClientManager {

    private final UniqueClientList clients;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        clients = new UniqueClientList();
    }

    public ClientManager() {}

    /**
     * Creates a ClientManager using the Clients in the {@code toBeCopied}
     */
    public ClientManager(ReadOnlyClientManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the client list with {@code clients}.
     * {@code clients} must not contain duplicate clients.
     */
    public void setClients(List<Client> clients) {
        this.clients.setClients(clients);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyClientManager newData) {
        requireNonNull(newData);
        setClients(newData.getClientList());
    }

    //// client-level operations

    /**
     * Returns true if a client with the same identity as {@code client} exists in the homerce book.
     */
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return clients.contains(client);
    }

    /**
     * Returns true if a client with the same phone number as {@code phone} exists in the homerce book.
     */
    public boolean checkClientWithPhone(Phone phone) {
        requireNonNull(phone);
        return clients.contains(phone);
    }

    /**
     * Adds a client to the homerce book.
     * The client must not already exist in the homerce book.
     */
    public void addClient(Client p) {
        clients.add(p);
    }

    /**
     * Replaces the given client {@code target} in the list with {@code editedClient}.
     * {@code target} must exist in the homerce book.
     * The client identity of {@code editedClient} must not be the same as another existing client in the homerce book.
     */
    public void setClient(Client target, Client editedClient) {
        requireNonNull(editedClient);

        clients.setClient(target, editedClient);
    }

    public Client getClientByPhone(Phone phone) {
        requireNonNull(phone);
        return clients.getClientByPhone(phone);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the homerce book.
     */
    public void removeClient(Client key) {
        clients.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return clients.asUnmodifiableObservableList().size() + " clients";
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clients.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ClientManager // instanceof handles nulls
                && clients.equals(((ClientManager) other).clients));
    }

    @Override
    public int hashCode() {
        return clients.hashCode();
    }

    /**
     * Creates a deep copy of all the clients in the unique list of clients.
     *
     * @return a client manager with a list of deep copied clients.
     */
    public ClientManager deepCopy() {
        List<Client> internalListCopy = clients.deepCopy();
        ClientManager clientManagerCopy = new ClientManager();
        clientManagerCopy.setClients((internalListCopy));
        return clientManagerCopy;
    }
}

