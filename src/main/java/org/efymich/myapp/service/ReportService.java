package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.ReportDAO;
import org.efymich.myapp.entity.Report;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ReportService {
    private ReportDAO reportDAO;

    public List<Report> getAll() {
        return reportDAO.getAll();
    }
    public List<Report> getAll(String sortParameter) {
        return reportDAO.getAll(sortParameter);
    }

    public Report getById(Long id) {
        return reportDAO.getById(id);
    }

    public void create(Report student) {
        reportDAO.create(student);
    }

    public void update(Report updatedStudent) {
        reportDAO.update(updatedStudent);
    }

    public void delete(Long id) {
        reportDAO.delete(id);
    }

    public Set<String> getColumnNames(Class<Report> reportClass) {
        return reportDAO.getColumnNames(reportClass);
    }

    public List<Report> getByStudentId(Long studentId) {
        return reportDAO.getByStudentId(studentId);
    }
}
