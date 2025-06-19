import java.io.*;
import java.util.*;

class Book {
    String id, title, author;

    Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return id + "," + title + "," + author;
    }

    static Book fromString(String line) {
        String[] parts = line.split(",", 3);
        return new Book(parts[0], parts[1], parts[2]);
    }
}

class LibraryInventory {
    static final String FILE_NAME = "books.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Library Inventory ---");
            System.out.println("1. Add Book");
            System.out.println("2. Delete Book");
            System.out.println("3. Search Book");
            System.out.println("4. List All Books");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> deleteBook();
                case 3 -> searchBook();
                case 4 -> listBooks();
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        Book book = new Book(id, title, author);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(book.toString());
            writer.newLine();
            System.out.println("Book added!");
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    static void deleteBook() {
        System.out.print("Enter Book ID to delete: ");
        String id = scanner.nextLine();
        List<Book> books = loadBooks();
        boolean removed = books.removeIf(book -> book.id.equals(id));

        if (removed) {
            saveBooks(books);
            System.out.println("Book deleted.");
        } else {
            System.out.println("Book not found.");
        }
    }

    static void searchBook() {
        System.out.print("Enter title or author keyword: ");
        String keyword = scanner.nextLine().toLowerCase();
        List<Book> books = loadBooks();

        System.out.println("\nSearch Results:");
        boolean found = false;
        for (Book b : books) {
            if (b.title.toLowerCase().contains(keyword) || b.author.toLowerCase().contains(keyword)) {
                System.out.println(b.id + " | " + b.title + " | " + b.author);
                found = true;
            }
        }
        if (!found) System.out.println("No books matched.");
    }

    static void listBooks() {
        List<Book> books = loadBooks();
        System.out.println("\n--- All Books ---");
        for (Book b : books) {
            System.out.println(b.id + " | " + b.title + " | " + b.author);
        }
    }

    static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(Book.fromString(line));
            }
        } catch (IOException e) {
            // File might not exist yet — ignore
        }
        return books;
    }

    static void saveBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
                writer.write(b.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }
}
