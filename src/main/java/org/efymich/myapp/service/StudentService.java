package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.StudentDAO;
import org.efymich.myapp.entity.Student;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class StudentService {
    private StudentDAO studentDAO;

    public List<Student> getAll(Integer currentPage, String sortParameter) {
        return studentDAO.getAll(currentPage, sortParameter);
    }

    public void create(Student student){
        studentDAO.create(student);
    }

    public void delete(Long id){
        studentDAO.delete(id);
    }

    public Long getAllCount(){ return studentDAO.getAllCount();}
    public Set<String> getColumnNames(Class<Student> studentClass) {
        return studentDAO.getColumnNames(studentClass);
    }
}
