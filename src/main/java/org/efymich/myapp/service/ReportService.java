package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.BookDAO;
import org.efymich.myapp.dao.ReportDAO;
import org.efymich.myapp.dao.StudentDAO;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Report;
import org.efymich.myapp.entity.Student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ReportService {
    private ReportDAO reportDAO;
    private BookDAO bookDAO;

    public List<Report> getAll() {
        return reportDAO.getAll();
    }
    public List<Report> getAll(String sortParameter) {
        return reportDAO.getAll(sortParameter);
    }

    public Report getById(Long id) {
        return reportDAO.getById(id);
    }

    public void create(Long bookId, Student student) {
        Report report = Report.builder()
                .book(bookDAO.getById(bookId))
                .student(student)
                .build();
        reportDAO.create(report);
    }

    public void update(Report updatedReport) {
        reportDAO.update(updatedReport);
    }

    public void delete(Long bookId,Student student) {
        Report reportOfHeldBook = reportDAO.getReportOfHeldBook(student.getStudentId(), bookId);
        reportDAO.delete(reportOfHeldBook.getRentalId());
    }

    public void deleteOldRecords(){
        reportDAO.deleteOldRecords();
    }

    public Set<String> getColumnNames(Class<Report> reportClass) {
        return reportDAO.getColumnNames(reportClass);
    }

    public List<Report> getByStudentId(Long studentId) {
        return reportDAO.getByStudentId(studentId);
    }

    public void giveBookBack(Long bookId, Student student){
        Report reportOfHeldBook = reportDAO.getReportOfHeldBook(student.getStudentId(), bookId);
        reportOfHeldBook.setReturnDate(LocalDateTime.now());
        reportDAO.update(reportOfHeldBook);
    }
}
