package org.efymich.myapp.dao;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Report;
import org.efymich.myapp.utils.Constants;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookDAO implements BaseDAO<Book>{
    private SessionFactory sessionFactory;

    @Override
    public List<Book> getAll() {
        Session session = sessionFactory.openSession();
        Query<Book> query = session.createQuery("From Book b order by b.bookId", Book.class);
        return query.getResultList();
    }

    public List<Book> getAll(Integer currentPage, String sort) {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Book> query = builder.createQuery(Book.class);
        JpaRoot<Book> root = query.from(Book.class);

        if (sort != null && !sort.isEmpty()) {
            query.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Book> booksQuery = session.createQuery(query);
        booksQuery.setFirstResult((currentPage - 1) * Constants.RECORDS_PER_PAGE);
        booksQuery.setMaxResults(Constants.RECORDS_PER_PAGE);
        return booksQuery.getResultList();
    }

    public Long getAllCount(){
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Long> query = builder.createQuery(Long.class);
        JpaRoot<Book> root = query.from(Book.class);

        query.select(builder.count(root));
        return session.createQuery(query).getSingleResult();
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

    public List<Book> getAllFreeBooks(Integer currentPage, String sort){
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Book> query = builder.createQuery(Book.class);
        JpaRoot<Book> root = query.from(Book.class);
        JpaJoin<Book, Report> join = root.join("report", JoinType.LEFT);
        join.on(builder.isNull(join.get("returnDate")));
        query.select(root).where(builder.isNull(join.get("rentalId")));

        if (sort != null && !sort.isEmpty()) {
            query.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Book> bookQuery = session.createQuery(query);
        bookQuery.setFirstResult((currentPage - 1) * Constants.RECORDS_PER_PAGE);
        bookQuery.setMaxResults(Constants.RECORDS_PER_PAGE);
        return bookQuery.getResultList();
    }
}
