package org.efymich.myapp.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
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
        Query<Book> query = session.createQuery("From Book b order by b.id", Book.class);
        return query.getResultList();
    }

    public List<Book> getAll(String sort, Integer currentPage) {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Book> critQuery = builder.createQuery(Book.class);
        JpaRoot<Book> root = critQuery.from(Book.class);

        if (sort != null && !sort.isEmpty()) {
            critQuery.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Book> booksQuery = session.createQuery(critQuery);
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

    public List<Book> getAllFreeBooks(String sort, Integer currentPage){
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Book> query = builder.createQuery(Book.class);
        JpaRoot<Book> root = query.from(Book.class);
        JpaJoin<Object, Object> join = root.join("report", JoinType.LEFT);
        JpaPredicate predicate = builder.isNull(join.get("returnDate"));
        join.on(predicate);

        if (sort != null && !sort.isEmpty()) {
            query.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Book> query1 = session.createQuery(query);

        query1.setFirstResult((currentPage - 1) * Constants.RECORDS_PER_PAGE);
        query1.setMaxResults(Constants.RECORDS_PER_PAGE);

//        Query<Book> query = session.createQuery("SELECT b FROM Book b " +
//                "LEFT JOIN Report r ON b.id = r.book.id AND r.returnDate IS NULL " +
//                "WHERE r.rentalId is null",Book.class);
//        query.setFirstResult((currentPage - 1) * Constants.RECORDS_PER_PAGE);
//        query.setMaxResults(Constants.RECORDS_PER_PAGE);
        return query1.getResultList();
    }
}
