package org.efymich.myapp.dao;

import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@AllArgsConstructor
public class StudentDAO {
    private SessionFactory sessionFactory;

    public List<Student> getStudents() {
        Session session = sessionFactory.openSession();
        Query<Student> students = session.createQuery("From Student", Student.class);
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
        updatingStudent.setMajor(updatedStudent.getMajor());
        transaction.commit();
    }

    public void delete(Long id){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(getById(id));
        transaction.commit();
    }
}
