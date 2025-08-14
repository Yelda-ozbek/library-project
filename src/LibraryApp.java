import Model.Book;
import Service.Catalog;
import Service.InvoiceService;
import java.util.*;
public class LibraryApp {

    // Üyenin ödünç aldığı kitaplar ve limit kontrolü için
    static class MemberRecord {
        private long memberId;
        private Set<Long> borrowedBookIds = new HashSet<>();
        private final int maxLimit = 5;

        public MemberRecord(long memberId) {
            this.memberId = memberId;
        }

        public boolean canBorrow() {
            return borrowedBookIds.size() < maxLimit;
        }

        public void borrowBook(long bookId) {
            borrowedBookIds.add(bookId);
        }

        public void returnBook(long bookId) {
            borrowedBookIds.remove(bookId);
        }

        public int getBorrowedCount() {
            return borrowedBookIds.size();
        }

        public Set<Long> getBorrowedBookIds() {
            return borrowedBookIds;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Catalog catalog = new Catalog();
        InvoiceService invoiceService = new InvoiceService();


        Map<Long, MemberRecord> memberRecords = new HashMap<>();


        catalog.addBook(new Book(1, "Sefiller", "Victor Hugo", "Classics", 20.0));
        catalog.addBook(new Book(2, "Suç ve Ceza", "Dostoyevski", "Classics", 18.0));
        catalog.addBook(new Book(3, "Kürk Mantolu Madonna", "Sabahattin Ali", "Roman", 15.0));

        System.out.println("Kütüphane sistemine hoşgeldiniz!");

        while (true) {
            System.out.println("Menü:");
            System.out.println("1. Tüm Kitapları Listele");
            System.out.println("2. Kitap Ekle");
            System.out.println("3. Kitap Sil");
            System.out.println("4. Kitap Ara (ID / İsim / Yazar)");
            System.out.println("5. Kitap Ödünç Al");
            System.out.println("6. Kitap İade Et");
            System.out.println("7. Üyenin Ödünç Aldığı Kitapları Listele");
            System.out.println("8. Fatura Listele");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Lütfen geçerli bir sayı girin.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.println("Tüm kitaplar:");
                        catalog.listAll().forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Kitap ID: ");
                        long id = Long.parseLong(scanner.nextLine());
                        System.out.print("Kitap Başlığı: ");
                        String title = scanner.nextLine();
                        System.out.print("Yazar: ");
                        String author = scanner.nextLine();
                        System.out.print("Kategori: ");
                        String category = scanner.nextLine();
                        System.out.print("Fiyat: ");
                        double price = Double.parseDouble(scanner.nextLine());

                        catalog.addBook(new Book(id, title, author, category, price));
                        System.out.println("Kitap eklendi.");
                    }
                    case 3 -> {
                        System.out.print("Silinecek Kitap ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        catalog.deleteBook(id);
                        System.out.println("Kitap silindi.");
                    }
                    case 4 -> {
                        System.out.println("Arama türü seçin: 1-ID, 2-İsim, 3-Yazar");
                        int searchType = Integer.parseInt(scanner.nextLine());
                        switch (searchType) {
                            case 1 -> {
                                System.out.print("Kitap ID: ");
                                int id = Integer.parseInt(scanner.nextLine());
                                var book = catalog.findById(id);
                                System.out.println(book);
                            }
                            case 2 -> {
                                System.out.print("Kitap İsmi: ");
                                String title = scanner.nextLine();
                                var books = catalog.findByTitle(title);
                                books.forEach(System.out::println);
                            }
                            case 3 -> {
                                System.out.print("Yazar İsmi: ");
                                String author = scanner.nextLine();
                                var books = catalog.findByAuthor(author);
                                books.forEach(System.out::println);
                            }
                            default -> System.out.println("Geçersiz arama türü.");
                        }
                    }
                    case 5 -> { // Kitap Ödünç Alma
                        System.out.print("Üye ID: ");
                        long memberId = Long.parseLong(scanner.nextLine());
                        System.out.print("Ödünç alınacak kitap ID: ");
                        long bookId = Long.parseLong(scanner.nextLine());

                        MemberRecord member = memberRecords.computeIfAbsent(memberId, MemberRecord::new);
                        Book book = catalog.findById((int) bookId);

                        if (!book.isAvailable()) {
                            System.out.println("Kitap zaten ödünç verilmiş.");
                            break;
                        }
                        if (!member.canBorrow()) {
                            System.out.println("Üye maksimum ödünç kitap sayısına ulaştı.");
                            break;
                        }

                        // Ödünç alma işlemi
                        book.setAvailable(false);
                        member.borrowBook(bookId);

                        var invoice = invoiceService.createInvoice(book);
                        System.out.println("Kitap ödünç alındı. Fatura: " + invoice);
                    }
                    case 6 -> { // Kitap İade
                        System.out.print("Üye ID: ");
                        long memberId = Long.parseLong(scanner.nextLine());
                        System.out.print("İade edilecek kitap ID: ");
                        long bookId = Long.parseLong(scanner.nextLine());

                        MemberRecord member = memberRecords.get(memberId);
                        if (member == null || !member.getBorrowedBookIds().contains(bookId)) {
                            System.out.println("Bu üye bu kitabı ödünç almamış.");
                            break;
                        }

                        Book book = catalog.findById((int) bookId);
                        book.setAvailable(true);
                        member.returnBook(bookId);

                        // İade için ücret iadesi (fatura listesinden çıkarma veya ayrıca işlem yapılabilir)
                        System.out.println("Kitap iade edildi, ücret iadesi yapıldı.");
                    }
                    case 7 -> {
                        System.out.print("Üye ID: ");
                        long memberId = Long.parseLong(scanner.nextLine());
                        MemberRecord member = memberRecords.get(memberId);
                        if (member == null || member.getBorrowedBookIds().isEmpty()) {
                            System.out.println("Bu üye hiç kitap ödünç almamış.");
                        } else {
                            System.out.println("Üyenin ödünç aldığı kitaplar:");
                            for (long bId : member.getBorrowedBookIds()) {
                                System.out.println(catalog.findById((int) bId));
                            }
                        }
                    }
                    case 8 -> {
                        System.out.println("Tüm faturalar:");
                        invoiceService.getAllInvoices().forEach(System.out::println);
                    }
                    case 0 -> {
                        System.out.println("Sistemden çıkılıyor...");
                        System.exit(0);
                    }
                    default -> System.out.println("Geçersiz seçim.");
                }
            } catch (Exception e) {
                System.out.println("Hata: " + e.getMessage());
            }

        }
    }
}
