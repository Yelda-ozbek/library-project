package Model;

public class Librarian extends Person {

    public Librarian(long id, String name) { super(id, name); }

    @Override
    public String whoAreYou() {
        return "Librarian: " + getName();
    }

    //kendime not : İleride kütüphane yönetimi ile ilgili ekstra metotlar
}
