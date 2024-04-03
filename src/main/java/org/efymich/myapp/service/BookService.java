package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.BookDAO;
import org.efymich.myapp.entity.Book;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class BookService {
    private BookDAO bookDAO;

    public List<Book> getAll(String sortParameter) {
        return bookDAO.getAll(sortParameter);
    }

    public Book getById(Long id) {
        return bookDAO.getById(id);
    }

    public void create(Book student) {
        bookDAO.create(student);
    }

    public void update(Book updatedStudent) {
        bookDAO.update(updatedStudent);
    }

    public void delete(Long id) {
        bookDAO.delete(id);
    }

    public Set<String> getColumnNames(Class<Book> bookClass) {
       return bookDAO.getColumnNames(bookClass);
    }
}
