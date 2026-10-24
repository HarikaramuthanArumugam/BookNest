import java.util.Scanner;

public class LibraryApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Create objects for all seven DAO classes
        AddBookDAO addBookDAO = new AddBookDAO();
        RegisterMemberDAO registerMemberDAO = new RegisterMemberDAO();
        IssueBookDAO issueBookDAO = new IssueBookDAO();
        ReturnBookDAO returnBookDAO = new ReturnBookDAO();
        SearchBookDAO searchBookDAO = new SearchBookDAO();
        ShowAllBooksDAO showAllBooksDAO = new ShowAllBooksDAO();
        ShowSummaryDAO showSummaryDAO = new ShowSummaryDAO();

        int choice;

        do {

            System.out.println("\n===== BookNest Library =====");
            System.out.println("1. Add Book");
            System.out.println("2. Register Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book");
            System.out.println("6. Show All Books");
            System.out.println("7. Show Summary");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Enter a number.");
                sc.nextLine();
                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // Case 1: Add Book
                case 1: {

                    System.out.print("Enter Book ID: ");
                    String bookId = sc.nextLine().trim();

                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine().trim();

                    System.out.print("Enter Author Name: ");
                    String author = sc.nextLine().trim();

                    System.out.print("Enter Total Copies: ");

                    if (!sc.hasNextInt()) {
                        System.out.println(
                                "Invalid input! Copies must be a number.");
                        sc.nextLine();
                        break;
                    }

                    int copies = sc.nextInt();
                    sc.nextLine();

                    if (bookId.isEmpty() || title.isEmpty()
                            || author.isEmpty() || copies <= 0) {

                        System.out.println(
                                "Invalid details! Fields cannot be empty "
                                + "and copies must be positive.");
                        break;
                    }

                    Book book = new Book(
                            bookId, title, author, copies, copies);

                    addBookDAO.addBook(book);

                    break;
                }

                // Case 2: Register Member
                case 2: {

                    System.out.print("Enter Member ID: ");
                    String memberId = sc.nextLine().trim();

                    System.out.print("Enter Member Name: ");
                    String name = sc.nextLine().trim();

                    if (memberId.isEmpty() || name.isEmpty()) {
                        System.out.println(
                                "Member ID and name cannot be empty!");
                        break;
                    }

                    Member member = new Member(memberId, name);

                    registerMemberDAO.registerMember(member);

                    break;
                }

                // Case 3: Issue Book
                case 3: {

                    System.out.print("Enter Member ID: ");
                    String memberId = sc.nextLine().trim();

                    System.out.print("Enter Book ID: ");
                    String bookId = sc.nextLine().trim();

                    if (memberId.isEmpty() || bookId.isEmpty()) {
                        System.out.println(
                                "Member ID and Book ID cannot be empty!");
                        break;
                    }

                    issueBookDAO.issueBook(memberId, bookId);

                    break;
                }

                // Case 4: Return Book
                case 4: {

                    System.out.println("\n===== Return Book =====");

                    System.out.print("Enter Member ID: ");
                    String memberId = sc.nextLine().trim();

                    System.out.print("Enter Book ID: ");
                    String bookId = sc.nextLine().trim();

                    if (memberId.isEmpty() || bookId.isEmpty()) {
                        System.out.println(
                                "Member ID and Book ID cannot be empty!");
                        break;
                    }

                    returnBookDAO.returnBook(memberId, bookId);

                    break;
                }

                // Case 5: Search Book
                case 5: {

                    System.out.println("\n===== Search Book =====");
                    System.out.println("1. Search by Book ID");
                    System.out.println("2. Search by Book Title");
                    System.out.print("Choose search method: ");

                    if (!sc.hasNextInt()) {
                        System.out.println(
                                "Invalid input! Enter 1 or 2.");
                        sc.nextLine();
                        break;
                    }

                    int searchChoice = sc.nextInt();
                    sc.nextLine();

                    if (searchChoice == 1) {

                        System.out.print("Enter Book ID: ");
                        String bookId = sc.nextLine().trim();

                        if (bookId.isEmpty()) {
                            System.out.println(
                                    "Book ID cannot be empty!");
                        } else {
                            searchBookDAO.searchBook(bookId);
                        }

                    } else if (searchChoice == 2) {

                        System.out.print("Enter Book Title: ");
                        String title = sc.nextLine().trim();

                        if (title.isEmpty()) {
                            System.out.println(
                                    "Book title cannot be empty!");
                        } else {
                            searchBookDAO.searchBook(title, true);
                        }

                    } else {
                        System.out.println(
                                "Invalid search choice! Select 1 or 2.");
                    }

                    break;
                }

                // Case 6: Show All Books
                case 6:

                    showAllBooksDAO.showAllBooks();
                    break;

                // Case 7: Show Summary
                case 7:

                    showSummaryDAO.showSummary();
                    break;

                // Case 0: Exit
                case 0:

                    System.out.println(
                            "Thank you for using BookNest!");
                    break;

                // Invalid menu choice
                default:

                    System.out.println(
                            "Invalid choice! Select 0 to 7.");
            }

        } while (choice != 0);

        sc.close();
    }
}