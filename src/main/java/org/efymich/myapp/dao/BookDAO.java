package org.efymich.myapp.dao;

import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@AllArgsConstructor
public class BookDAO {
    private SessionFactory sessionFactory;

    public List<Book> getBooks() {
        Session session = sessionFactory.openSession();
        Query<Book> books = session.createQuery("From Book", Book.class);
        return books.getResultList();
    }

    public Book getById(Long id) {
        Session session = sessionFactory.openSession();
        return session.get(Book.class, id);
    }

    public void create(Book book) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(book);
        transaction.commit();
    }

    public void update(Book updatedBook) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Book updatingBook = getById(updatedBook.getBookId());
        updatingBook.setAuthor(updatedBook.getAuthor());
        updatingBook.setTitle(updatedBook.getTitle());
        updatingBook.setGenre(updatedBook.getGenre());
        transaction.commit();
    }

    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(getById(id));
        transaction.commit();
    }
}
