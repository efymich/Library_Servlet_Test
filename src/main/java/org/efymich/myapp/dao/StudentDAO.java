package org.efymich.myapp.dao;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.utils.Constants;
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
public class StudentDAO implements BaseDAO<Student>{
    private SessionFactory sessionFactory;

    @Override
    public List<Student> getAll() {
        Session session = sessionFactory.openSession();
        Query<Student> query = session.createQuery("From Student", Student.class);
        return query.getResultList();
    }

    public List<Student> getAll(Integer currentPage, String sort) {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Student> query = builder.createQuery(Student.class);
        JpaRoot<Student> root = query.from(Student.class);

        if (sort != null && !sort.isEmpty()) {
            query.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Student> studentQuery = session.createQuery(query);
        studentQuery.setFirstResult((currentPage - 1) * Constants.RECORDS_PER_PAGE);
        studentQuery.setMaxResults(Constants.RECORDS_PER_PAGE);
        return studentQuery.getResultList();
    }

    @Override
    public Long getAllCount() {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Long> query = builder.createQuery(Long.class);
        JpaRoot<Student> root = query.from(Student.class);

        query.select(builder.count(root));
        return session.createQuery(query).getSingleResult();
    }

    public Student getById(Long id) {
        Session session = sessionFactory.openSession();
        return session.get(Student.class,id);
    }

    public void create(Student student){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(student);
        transaction.commit();
    }

    public void update(Student updatedStudent) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Student updatingStudent = getById(updatedStudent.getStudentId());
        updatingStudent.setStudentName(updatedStudent.getStudentName());
        transaction.commit();
    }

    public void delete(Long id){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(getById(id));
        transaction.commit();
    }

    public Student getByName(String studentName) {
        Session session = sessionFactory.openSession();
        Query<Student> query = session.createQuery("From Student s Where s.studentName = :name", Student.class);
        query.setParameter("name",studentName);
        return query.uniqueResult();
    }

    @Override
    public Set<String> getColumnNames(Class<Student> entityClass) {
        Metamodel metamodel = sessionFactory.getMetamodel();
        EntityType<Student> entity = metamodel.entity(entityClass);
        return entity.getAttributes().stream()
                .filter(x -> !x.isAssociation())
                .map(Attribute::getName)
                .filter(string -> !string.equals("password"))
                .collect(Collectors.toSet());
    }
}
