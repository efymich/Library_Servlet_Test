package org.efymich.myapp.dao;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class StudentDAO implements BaseDAO<Student>{
    private SessionFactory sessionFactory;

    @Override
    public List<Student> getAll() {
        return null;
    }

    public List<Student> getAll(String sortParameter) {
        Session session = sessionFactory.openSession();
        Query<Student> students = session.createQuery("From Student order by id", Student.class);
        return students.getResultList();
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
                .map(Attribute::getName).
                collect(Collectors.toSet());
    }
}
