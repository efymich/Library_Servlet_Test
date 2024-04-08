package org.efymich.myapp.dao;

import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Report;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ReportDAO implements BaseDAO<Report> {

    private SessionFactory sessionFactory;

    @Override
    public List<Report> getAll() {
        Session session = sessionFactory.openSession();
        Query<Report> reports = session.createQuery("From Report", Report.class);
        return reports.getResultList();
    }

    public List<Report> getAll(String sortParameter) {
        return null;
    }

    public Report getById(Long id) {
        Session session = sessionFactory.openSession();
        return session.get(Report.class, id);
    }

    public void create(Report report) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(report);
        transaction.commit();
    }

    public void update(Report updatedReport) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(updatedReport);
        transaction.commit();
    }

    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(getById(id));
        transaction.commit();
    }

    @Override
    public Set<String> getColumnNames(Class<Report> entityClass) {
        return null;
    }

    public List<Report> getByStudentId(Long studentId) {
        Session session = sessionFactory.openSession();
        Query<Report> query = session.createQuery("From Report r where r.student.id = :studentId ", Report.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    public List<Report> getByBookId(Long bookId) {
        Session session = sessionFactory.openSession();
        Query<Report> query = session.createQuery("From Report r where r.book.id = :bookId ", Report.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    public Report getReportOfHeldBook(Long studentId, Long bookId){
        List<Report> reportList = getByStudentId(studentId);
        return reportList.stream().filter(report -> report.getReturnDate() == null)
                .filter(report -> report.getBook().getBookId().equals(bookId))
                .findFirst().orElseThrow();
    }

}
