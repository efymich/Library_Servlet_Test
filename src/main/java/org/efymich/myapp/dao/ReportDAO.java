package org.efymich.myapp.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import org.efymich.myapp.entity.Report;
import org.efymich.myapp.utils.Constants;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ReportDAO implements BaseDAO<Report> {

    private SessionFactory sessionFactory;

    @Override
    public List<Report> getAll() {
        Session session = sessionFactory.openSession();
        Query<Report> reports = session.createQuery("From Report", Report.class);
        return reports.getResultList();
    }

    public List<Report> getAll(Integer currentPage, String sort) {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<org.efymich.myapp.entity.Report> query = builder.createQuery(Report.class);
        JpaRoot<Report> root = query.from(Report.class);

        if (sort != null && !sort.isEmpty()) {
            query.select(root).orderBy(builder.asc(root.get(sort)));
        }

        Query<Report> reportsQuery = session.createQuery(query);
        reportsQuery.setFirstResult((currentPage - 1) * Constants.RECORDS_PER_PAGE);
        reportsQuery.setMaxResults(Constants.RECORDS_PER_PAGE);
        return reportsQuery.getResultList();
    }

    @Override
    public Long getAllCount() {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Long> query = builder.createQuery(Long.class);
        JpaRoot<Report> root = query.from(Report.class);

        query.select(builder.count(root));
        return session.createQuery(query).getSingleResult();
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

    public void deleteOldRecords(){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<Report> criteriaDelete = builder.createCriteriaDelete(Report.class);
        Root<Report> root = criteriaDelete.from(Report.class);

        criteriaDelete.where(
                builder.and(
                        builder.isNotNull(root.get("returnDate")),
                        builder.lessThan(root.get("returnDate"), LocalDateTime.now().minusDays(1))
                )
        );

        session.createMutationQuery(criteriaDelete).executeUpdate();
        transaction.commit();
    }

    @Override
    public Set<String> getColumnNames(Class<Report> entityClass) {
        Metamodel metamodel = sessionFactory.getMetamodel();
        EntityType<Report> entity = metamodel.entity(entityClass);
        return entity.getAttributes().stream()
                .filter(x -> !x.isAssociation())
                .map(Attribute::getName)
                .collect(Collectors.toSet());
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
