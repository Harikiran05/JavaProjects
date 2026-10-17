public class LibraryManagement {
  static class Book {
    String isbn;
    String title;
    boolean available;
    
    Book(String isbn, String title) {
      this.isbn = isbn;
      this.title = title;
      this.available = true;
    }
  }

  static class Member {
    String memberId;
    String name;

    Member(String memberId, String name) {
      this.memberId = memberId;
      this.name = name;
    }
  }

  static class Loan {
    String loanId;
    Book book;
    Member member;
    int overdueDays;
    
    Loan(String loanId, LibraryManagement.Book book, LibraryManagement.Member member, int overdueDays) {
      this.loanId = loanId;
      this.book = book;
      this.member = member;
      this.overdueDays = overdueDays;
    }

    
    }
  

  static class Fine {
  
    int overdueDays;
    double dailyRate = 0.50;

    Fine(int overdueDays) {
      this.overdueDays = overdueDays;
    }

    double calculateAmount() {
      return overdueDays * dailyRate;
    }
  }

  static class Inventory {
  
    Book[] books;
    int count = 0;

    Inventory(int capacity) {
      books = new Book[capacity];
    }
    void addBook(Book book){
      if (count <  books.length) {
        books[count] = book;
        count++;
      } else {
        System.out.println("Inventory is Full.");
      }
    }

    Book findBook(String isbn) {
      for (int i = 0; i < count ; i++) {
        if (books[i].isbn.equals(isbn)) {
          return books[i];
        }
      }
      return null;
    }
    boolean borrowBook(String isbn) {
      Book book = findBook(isbn);

      if (book == null || !book.available) {
        return false;
      }

      book.available = false;
      return true;
    }

    void returnBook(String isbn) {
      Book book = findBook(isbn);

      if (book != null) {
        book.available = true;
      }
    }
  }


  public static void main(String[] args) {
    
    Book book = new Book("978-001", "Clean Code");
    Member member = new Member("M-101", "Abc");
    
    Inventory inventory = new Inventory(10);
    inventory.addBook(book);

    if (inventory.borrowBook("978-001")) {
      Loan loan = new Loan("L-101", book, member, 3);

      Fine fine = new Fine(loan.overdueDays);

      System.out.println("Book: " + loan.book.title);
      System.out.println("Member: " + loan.member.name);
      System.out.println("Loan ID: " + loan.loanId);
      System.out.println("Fine: " + fine.calculateAmount());

      inventory.returnBook("978-001");
    } else {
      System.out.println("Book is unavailable");
    }
    System.out.println("Book available: " + book.available);
    System.out.println();
  }
}