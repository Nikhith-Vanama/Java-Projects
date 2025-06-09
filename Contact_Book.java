import java.io.*;
import java.util.*;

class ContactBook {
    private static final String FILE_NAME = "contacts.dat";

    private List<Contact> contacts;

    public ContactBook() {
        contacts = loadContacts();
    }

    public static void main(String[] args) {
        ContactBook app = new ContactBook();
        app.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Contact Book ---");
            System.out.println("1. List Contacts");
            System.out.println("2. Add Contact");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> listContacts();
                case 2 -> addContact(scanner);
                case 3 -> editContact(scanner);
                case 4 -> deleteContact(scanner);
                case 5 -> saveContacts();
                default -> System.out.println("Invalid choice, try again.");
            }
        } while (choice != 5);

        System.out.println("Goodbye!");
        scanner.close();
    }

    private void listContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\nContacts:");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, contacts.get(i));
        }
    }

    private void addContact(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine().trim();

        contacts.add(new Contact(name, phone));
        System.out.println("Contact added.");
    }

    private void editContact(Scanner scanner) {
        listContacts();
        if (contacts.isEmpty()) return;

        System.out.print("Enter contact number to edit: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid contact number.");
            return;
        }

        Contact c = contacts.get(index);
        System.out.println("Editing: " + c);

        System.out.print("New name (leave blank to keep): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) c.setName(newName);

        System.out.print("New phone (leave blank to keep): ");
        String newPhone = scanner.nextLine().trim();
        if (!newPhone.isEmpty()) c.setPhone(newPhone);

        System.out.println("Contact updated.");
    }

    private void deleteContact(Scanner scanner) {
        listContacts();
        if (contacts.isEmpty()) return;

        System.out.print("Enter contact number to delete: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid contact number.");
            return;
        }

        Contact removed = contacts.remove(index);
        System.out.println("Deleted: " + removed);
    }

    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
            System.out.println("Contacts saved.");
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Contact> loadContacts() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading contacts, starting fresh.");
            return new ArrayList<>();
        }
    }
}

class Contact implements Serializable {
    private String name;
    private String phone;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
