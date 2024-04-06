package org.efymich.myapp.dao;

import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class AuthorDAO implements BaseDAO<Author>{
    private SessionFactory sessionFactory;

    @Override
    public List<Author> getAll() {
        Session session = sessionFactory.openSession();
        Query<Author> authors = session.createQuery("From Author", Author.class);
        return authors.getResultList();
    }

    public List<Author> getAll(String sortParameter) {
        return null;
    }

    public Author getById(Long id) {
        Session session = sessionFactory.openSession();
        return session.get(Author.class, id);
    }

    public void create(Author author) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(author);
        transaction.commit();
    }

    public void update(Author updatedAuthor) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Author updatingAuthor = getById(updatedAuthor.getAuthorId());
        updatingAuthor.setAuthorName(updatedAuthor.getAuthorName());
        updatingAuthor.setNationality(updatedAuthor.getNationality());
        transaction.commit();
    }

    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(getById(id));
        transaction.commit();
    }

    @Override
    public Set<String> getColumnNames(Class<Author> entityClass) {
        return null;
    }
}
