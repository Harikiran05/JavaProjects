import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LibraryManagement {

    enum MembershipType {
        STANDARD, PREMIUM
    }

    // ---------- BOOK ----------

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
            if (this == obj) return true;
            if (!(obj instanceof Book)) return false;

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

    // ---------- MEMBER ----------

    static final class Member {
        private final String memberId;
        private final String name;
        private final MembershipType membershipType;

        Member(
            String memberId,
            String name,
            MembershipType membershipType
        ) {
            this.memberId = Objects.requireNonNull(memberId);
            this.name = Objects.requireNonNull(name);
            this.membershipType =
                Objects.requireNonNull(membershipType);
        }

        String getMemberId() {
            return memberId;
        }

        String getName() {
            return name;
        }

        MembershipType getMembershipType() {
            return membershipType;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Member)) return false;

            Member other = (Member) obj;
            return memberId.equals(other.memberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberId);
        }
    }

    // ---------- PHYSICAL BOOK COPY ----------

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
            if (this == obj) return true;
            if (!(obj instanceof BookCopy)) return false;

            BookCopy other = (BookCopy) obj;
            return copyId.equals(other.copyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(copyId);
        }
    }

    // ---------- FINE VALUE OBJECT ----------

    static final class Fine {
        private final int overdueDays;
        private final BigDecimal amount;

        Fine(int overdueDays, BigDecimal amount) {
            if (overdueDays < 0) {
                throw new IllegalArgumentException(
                    "Overdue days cannot be negative."
                );
            }

            Objects.requireNonNull(amount);

            if (amount.signum() < 0) {
                throw new IllegalArgumentException(
                    "Fine amount cannot be negative."
                );
            }

            this.overdueDays = overdueDays;
            this.amount = amount.setScale(
                2, RoundingMode.HALF_UP
            );
        }

        int getOverdueDays() {
            return overdueDays;
        }

        BigDecimal getAmount() {
            return amount;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Fine)) return false;

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

    // ---------- STRATEGY PATTERN ----------

    interface FineStrategy {
        BigDecimal calculate(int overdueDays);
    }

    static final class StandardFineStrategy
        implements FineStrategy {

        @Override
        public BigDecimal calculate(int overdueDays) {
            if (overdueDays < 0) {
                throw new IllegalArgumentException(
                    "Overdue days cannot be negative."
                );
            }

            return new BigDecimal("0.50")
                .multiply(BigDecimal.valueOf(overdueDays))
                .setScale(2, RoundingMode.HALF_UP);
        }
    }

    static final class PremiumFineStrategy
        implements FineStrategy {

        @Override
        public BigDecimal calculate(int overdueDays) {
            if (overdueDays < 0) {
                throw new IllegalArgumentException(
                    "Overdue days cannot be negative."
                );
            }

            return new BigDecimal("0.25")
                .multiply(BigDecimal.valueOf(overdueDays))
                .setScale(2, RoundingMode.HALF_UP);
        }
    }

    // ---------- LOAN ----------

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

        Fine close(int overdueDays, FineStrategy strategy) {
            if (!active) {
                throw new IllegalStateException(
                    "Loan has already been returned."
                );
            }

            Objects.requireNonNull(strategy);

            // Calculate before changing loan/copy state.
            BigDecimal amount =
                strategy.calculate(overdueDays);

            Fine calculatedFine =
                new Fine(overdueDays, amount);

            copy.markReturned();
            fine = calculatedFine;
            active = false;

            return fine;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Loan)) return false;

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

    // ---------- FACTORY PATTERN ----------

    static final class LoanFactory {

        Loan createLoan(
            String loanId,
            Member member,
            BookCopy copy
        ) {
            return new Loan(loanId, member, copy);
        }
    }

    // ---------- INVENTORY ----------

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
    }

    // ---------- OBSERVER PATTERN ----------

    interface LoanObserver {
        void onLibraryEvent(String event, Loan loan);
    }

    static final class ConsoleLoanObserver
        implements LoanObserver {

        @Override
        public void onLibraryEvent(String event, Loan loan) {

            if ("LOAN_CREATED".equals(event)) {
                System.out.println(
                    "Observer: Loan created: "
                        + loan.getLoanId()
                        + ", member: "
                        + loan.getMember().getName()
                );

            } else if ("LOAN_RETURNED".equals(event)) {
                String amount = loan.getFine().isPresent()
                    ? loan.getFine().get()
                        .getAmount().toPlainString()
                    : "not calculated";

                System.out.println(
                    "Observer: Loan returned: "
                        + loan.getLoanId()
                        + ", fine: " + amount
                );
            }
        }
    }

    // ---------- LIBRARY SERVICE ----------

    static final class LibraryService {
        private final Inventory inventory;
        private final LoanFactory loanFactory;

        private final FineStrategy standardStrategy;
        private final FineStrategy premiumStrategy;

        private final List<LoanObserver> observers =
            new ArrayList<>();

        LibraryService(
            Inventory inventory,
            LoanFactory loanFactory,
            FineStrategy standardStrategy,
            FineStrategy premiumStrategy
        ) {
            this.inventory = Objects.requireNonNull(inventory);
            this.loanFactory =
                Objects.requireNonNull(loanFactory);
            this.standardStrategy =
                Objects.requireNonNull(standardStrategy);
            this.premiumStrategy =
                Objects.requireNonNull(premiumStrategy);
        }

        void addObserver(LoanObserver observer) {
            observers.add(Objects.requireNonNull(observer));
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

            // Factory creates the Loan object.
            Loan loan = loanFactory.createLoan(
                loanId, member, copy
            );

            copy.markBorrowed();

            // Observer pattern announces the event.
            notifyObservers("LOAN_CREATED", loan);

            return loan;
        }

        Fine returnLoan(Loan loan, int overdueDays) {

            FineStrategy selectedStrategy =
                loan.getMember().getMembershipType()
                    == MembershipType.PREMIUM
                ? premiumStrategy
                : standardStrategy;

            Fine fine = loan.close(
                overdueDays, selectedStrategy
            );

            notifyObservers("LOAN_RETURNED", loan);

            return fine;
        }

        private void notifyObservers(
            String event, Loan loan
        ) {
            for (LoanObserver observer : observers) {
                observer.onLibraryEvent(event, loan);
            }
        }
    }

    // ---------- PROGRAM ENTRY POINT ----------

    public static void main(String[] args) {

        Book book = new Book(
            "978-001", "Clean Code", "Robert C. Martin"
        );

        Member member = new Member(
            "M-101", "Asha", MembershipType.PREMIUM
        );

        BookCopy copy = new BookCopy("C-001", book);

        Inventory inventory = new Inventory();
        inventory.addCopy(copy);

        LoanFactory factory = new LoanFactory();

        FineStrategy standardStrategy =
            new StandardFineStrategy();

        FineStrategy premiumStrategy =
            new PremiumFineStrategy();

        LibraryService service = new LibraryService(
            inventory,
            factory,
            standardStrategy,
            premiumStrategy
        );

        service.addObserver(new ConsoleLoanObserver());

        Loan loan = service.issueLoan(
            "L-101", member, "978-001"
        );

        Fine fine = service.returnLoan(loan, 4);

        System.out.println("Final fine: " + fine.getAmount());
        System.out.println("Loan active: " + loan.isActive());
        System.out.println("Copy available: " + copy.isAvailable());
        System.out.println();
    }
}