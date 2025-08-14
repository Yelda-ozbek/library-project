package com.library.repository;

import Model.Author;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    private List<Author> authors = new ArrayList<>();

    public void addAuthor(Author author) { authors.add(author); }

    public List<Author> getAllAuthors() { return authors; }
}
