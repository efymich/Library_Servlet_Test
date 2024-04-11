package org.efymich.myapp.dao;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Author;
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
public class AuthorDAO implements BaseDAO<Author>{
    private SessionFactory sessionFactory;

    @Override
    public List<Author> getAll() {
        Session session = sessionFactory.openSession();
        Query<Author> authors = session.createQuery("From Author", Author.class);
        return authors.getResultList();
    }

    @Override
    public Long getAllCount() {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Long> query = builder.createQuery(Long.class);
        JpaRoot<Author> root = query.from(Author.class);

        query.select(builder.count(root));
        return session.createQuery(query).getSingleResult();
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
        Metamodel metamodel = sessionFactory.getMetamodel();
        EntityType<Author> entity = metamodel.entity(entityClass);
        return entity.getAttributes().stream()
                .filter(x -> !x.isAssociation())
                .map(Attribute::getName)
                .collect(Collectors.toSet());
    }
}
