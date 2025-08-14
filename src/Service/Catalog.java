package Service;
import Model.Book;
import java.util.List;
import Exception.AuthorNotFoundException;
import Exception.BookNotFoundException;
import Repository.BookRepository;

public class Catalog {
    private BookRepository repository = new BookRepository();

    public void addBook(Book book) {
        repository.save(book);
    }

    public List<Book> listAll() {
        return repository.findAll();
    }

    public List<Book> findByAuthor(String author) {
        List<Book> books = repository.findByAuthor(author);
        if (books.isEmpty()) {
            throw new AuthorNotFoundException("Yazar bulunamadı: " + author);
        }
        return books;
    }


    public Book findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı: " + id));
    }
    public List<Book> findByTitle(String title) {
        List<Book> books = repository.findByTitle(title);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Kitap bulunamadı: " + title);
        }
        return books;
    }
    public void deleteBook(int id) {
        repository.deleteById(id);
    }
}
