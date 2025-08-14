package Model;

public class Member extends Person {
    private int maxBooks = 5;

    public Member() { super(); }
    public Member(long id, String name) { super(id, name); }

    @Override
    public String whoAreYou() {
        return "Member: " + getName();
    }

    public int getMaxBooks() { return maxBooks; }
    public void setMaxBooks(int maxBooks) { this.maxBooks = maxBooks; }
}
