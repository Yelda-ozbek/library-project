package Model;

import java.util.Date;

public class Invoice {
    private long memberId;
    private long bookId;
    private double amount;
    private Date issueDate;

    public Invoice(long memberId, long bookId, double amount, Date issueDate) {
        this.memberId = memberId; this.bookId = bookId; this.amount = amount; this.issueDate = issueDate;
    }

    public Invoice(int memberId, Book book) {
        this.memberId = memberId;
        this.bookId = book.getId();
        this.amount = book.getPrice();
        this.issueDate = new Date();
    }


    public long getMemberId() { return memberId; }
    public long getBookId() { return bookId; }
    public double getAmount() { return amount; }
    public Date getIssueDate() { return issueDate; }

    @Override
    public String toString() {
        return "Invoice{member=" + memberId + ", book=" + bookId + ", amount=" + amount + ", date=" + issueDate + "}";
    }
}
