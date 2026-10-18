import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LibraryManagement {

    // Immutable catalog information
    static final class Book {
        private final String isbn;
        private final String title;
        private final String author;

        Book(String isbn, String title, String author) {
            this.isbn = Objects.requireNonNull(isbn);
            this.title = Objects.requireNonNull(title);
            this.author = Objects.requireNonNull(author);
        }

        String getIsbn() {
            return isbn;
        }

        String getTitle() {
            return title;
        }

        String getAuthor() {
            return author;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Book)) {
                return false;
            }

            Book other = (Book) obj;
            return isbn.equals(other.isbn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isbn);
        }

        @Override
        public String toString() {
            return title + " (" + isbn + ")";
        }
    }

    // Immutable member details
    static final class Member {
        private final String memberId;
        private final String name;

        Member(String memberId, String name) {
            this.memberId = Objects.requireNonNull(memberId);
            this.name = Objects.requireNonNull(name);
        }

        String getMemberId() {
            return memberId;
        }

        String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Member)) {
                return false;
            }

            Member other = (Member) obj;
            return memberId.equals(other.memberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberId);
        }
    }

    // One physical copy of a catalog book
    static final class BookCopy {
        private final String copyId;
        private final Book book;
        private boolean available = true;

        BookCopy(String copyId, Book book) {
            this.copyId = Objects.requireNonNull(copyId);
            this.book = Objects.requireNonNull(book);
        }

        String getCopyId() {
            return copyId;
        }

        Book getBook() {
            return book;
        }

        boolean isAvailable() {
            return available;
        }

        void markBorrowed() {
            if (!available) {
                throw new IllegalStateException(
                    "Copy is already borrowed."
                );
            }

            available = false;
        }

        void markReturned() {
            if (available) {
                throw new IllegalStateException(
                    "Copy is already available."
                );
            }

            available = true;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof BookCopy)) {
                return false;
            }

            BookCopy other = (BookCopy) obj;
            return copyId.equals(other.copyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(copyId);
        }
    }

    // Immutable fine value
    static final class Fine {
        private final int overdueDays;
        private final BigDecimal amount;

        Fine(int overdueDays, BigDecimal dailyRate) {
            if (overdueDays < 0) {
                throw new IllegalArgumentException(
                    "Overdue days cannot be negative."
                );
            }

            Objects.requireNonNull(dailyRate);

            if (dailyRate.signum() < 0) {
                throw new IllegalArgumentException(
                    "Fine rate cannot be negative."
                );
            }

            this.overdueDays = overdueDays;
            this.amount = dailyRate
                .multiply(BigDecimal.valueOf(overdueDays))
                .setScale(2, RoundingMode.HALF_UP);
        }

        int getOverdueDays() {
            return overdueDays;
        }

        BigDecimal getAmount() {
            return amount;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Fine)) {
                return false;
            }

            Fine other = (Fine) obj;

            return overdueDays == other.overdueDays
                && amount.equals(other.amount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(overdueDays, amount);
        }

        @Override
        public String toString() {
            return "Fine{days=" + overdueDays
                + ", amount=" + amount + "}";
        }
    }

    // A loan has a lifecycle: active -> returned
    static final class Loan {
        private final String loanId;
        private final Member member;
        private final BookCopy copy;

        private boolean active = true;
        private Fine fine;

        Loan(String loanId, Member member, BookCopy copy) {
            this.loanId = Objects.requireNonNull(loanId);
            this.member = Objects.requireNonNull(member);
            this.copy = Objects.requireNonNull(copy);
        }

        String getLoanId() {
            return loanId;
        }

        Member getMember() {
            return member;
        }

        BookCopy getCopy() {
            return copy;
        }

        boolean isActive() {
            return active;
        }

        Optional<Fine> getFine() {
            return Optional.ofNullable(fine);
        }

        Fine close(int overdueDays, BigDecimal dailyRate) {
            if (!active) {
                throw new IllegalStateException(
                    "Loan has already been returned."
                );
            }

            Fine calculatedFine = new Fine(overdueDays, dailyRate);

            // Change state only after fine creation succeeds.
            copy.markReturned();
            fine = calculatedFine;
            active = false;

            return fine;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Loan)) {
                return false;
            }

            Loan other = (Loan) obj;
            return loanId.equals(other.loanId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(loanId);
        }

        @Override
        public String toString() {
            return "Loan{id=" + loanId
                + ", active=" + active + "}";
        }
    }

    // Inventory manages physical copies
    static final class Inventory {
        private final List<BookCopy> copies = new ArrayList<>();

        void addCopy(BookCopy copy) {
            Objects.requireNonNull(copy);

            if (copies.contains(copy)) {
                throw new IllegalArgumentException(
                    "Duplicate copy ID."
                );
            }

            copies.add(copy);
        }

        Optional<BookCopy> findAvailableCopy(String isbn) {
            for (BookCopy copy : copies) {
                if (copy.isAvailable()
                    && copy.getBook().getIsbn().equals(isbn)) {
                    return Optional.of(copy);
                }
            }

            return Optional.empty();
        }

        List<BookCopy> getCopies() {
            return Collections.unmodifiableList(
                new ArrayList<>(copies)
            );
        }
    }

    // Coordinates issue and return operations
    static final class LibraryService {
        private final Inventory inventory;
        private final BigDecimal dailyFineRate;

        LibraryService(
            Inventory inventory,
            BigDecimal dailyFineRate
        ) {
            this.inventory = Objects.requireNonNull(inventory);
            this.dailyFineRate =
                Objects.requireNonNull(dailyFineRate);

            if (dailyFineRate.signum() < 0) {
                throw new IllegalArgumentException(
                    "Fine rate cannot be negative."
                );
            }
        }

        Loan issueLoan(
            String loanId,
            Member member,
            String isbn
        ) {
            Optional<BookCopy> result =
                inventory.findAvailableCopy(isbn);

            if (!result.isPresent()) {
                throw new IllegalStateException(
                    "No available copy for ISBN: " + isbn
                );
            }

            BookCopy copy = result.get();
            Loan loan = new Loan(loanId, member, copy);

            copy.markBorrowed();

            return loan;
        }

        Fine returnLoan(Loan loan, int overdueDays) {
            return loan.close(overdueDays, dailyFineRate);
        }
    }

    public static void main(String[] args) {

        Book book = new Book(
            "978-001", "Clean Code", "Robert C. Martin"
        );

        Member member = new Member("M-101", "Asha");

        BookCopy copy = new BookCopy("C-001", book);

        Inventory inventory = new Inventory();
        inventory.addCopy(copy);

        LibraryService service = new LibraryService(
            inventory, new BigDecimal("0.50")
        );

        Loan loan = service.issueLoan(
            "L-101", member, "978-001"
        );

        System.out.println("Loan issued: " + loan);
        System.out.println("Copy available: " + copy.isAvailable());

        Fine fine = service.returnLoan(loan, 3);

        System.out.println("Fine: " + fine);
        System.out.println("Copy available: " + copy.isAvailable());
        System.out.println("Loan active: " + loan.isActive());

        Book sameBook = new Book(
            "978-001", "A Different Title", "Another Author"
        );

        System.out.println(
            "Books have same identity: " + book.equals(sameBook)
        );
    }
}