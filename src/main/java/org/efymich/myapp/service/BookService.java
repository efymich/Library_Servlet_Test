package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.BookDAO;
import org.efymich.myapp.entity.Book;
import org.efymich.myapp.entity.Report;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class BookService {
    private BookDAO bookDAO;
    private ReportService reportService;

    public List<Book> getAll(String sortParameter) {
        return bookDAO.getAll(sortParameter);
    }

    public Book getById(Long id) {
        return bookDAO.getById(id);
    }

    public void create(Book student) {
        bookDAO.create(student);
    }

    public void update(Book updatedStudent) {
        bookDAO.update(updatedStudent);
    }

    public void delete(Long id) {
        bookDAO.delete(id);
    }

    public Set<String> getColumnNames(Class<Book> bookClass) {
       return bookDAO.getColumnNames(bookClass);
    }

    public List<Book> getBooksHeldByStudent(Long studentId){
        List<Report> reportList = reportService.getByStudentId(studentId);
        return reportList.stream().filter(report -> report.getReturnDate() == null)
                .map(Report::getBook).toList();
    }

    public List<Book> getFreeBooks(){
        return bookDAO.getAllFreeBooks();
    }
}
