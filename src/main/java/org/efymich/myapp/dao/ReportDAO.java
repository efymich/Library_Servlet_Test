package org.efymich.myapp.dao;

import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Report;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ReportDAO implements BaseDAO<Report>{

    private SessionFactory sessionFactory;

    public List<Report> getAll() {
        Session session = sessionFactory.openSession();
        Query<Report> reports = session.createQuery("From Report", Report.class);
        return reports.getResultList();
    }

    @Override
    public List<Report> getAll(String sortParameter) {
        return null;
    }

    public Report getById(Long id) {
        Session session = sessionFactory.openSession();
        return session.get(Report.class,id);
    }

    public void create(Report report){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(report);
        transaction.commit();
    }

    public void update(Report updatedReport) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Report updatingReport = getById(updatedReport.getRentalId());
        updatingReport.setRentalDate(updatedReport.getRentalDate());
        updatingReport.setReturnDate(updatedReport.getReturnDate());
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

}
