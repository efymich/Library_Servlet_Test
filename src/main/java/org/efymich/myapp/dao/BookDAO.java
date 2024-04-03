package org.efymich.myapp.dao;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookDAO implements BaseDAO<Book>{
    private SessionFactory sessionFactory;

    public List<Book> getAll(String sort) {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Book> critQuery = builder.createQuery(Book.class);
        JpaRoot<Book> root = critQuery.from(Book.class);

        if (sort != null && !sort.isEmpty()) {
            critQuery.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Book> booksQuery = session.createQuery(critQuery);
        return booksQuery.getResultList();
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

    @Override
    public Set<String> getColumnNames(Class<Book> entityClass) {
        Metamodel metamodel = sessionFactory.getMetamodel();
        EntityType<Book> entity = metamodel.entity(entityClass);
        return entity.getAttributes().stream()
                .filter(x -> !x.isAssociation())
                .map(Attribute::getName)
                .collect(Collectors.toSet());
    }
}
