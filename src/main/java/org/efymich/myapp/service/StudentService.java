package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.StudentDAO;
import org.efymich.myapp.entity.Student;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class StudentService {
    private StudentDAO studentDAO;

    public List<Student> getAll(String sortParameter) {
        return studentDAO.getAll(sortParameter);
    }

    public Student getById(Long id) {
        return studentDAO.getById(id);
    }

    public void create(Student student){
        studentDAO.create(student);
    }

    public void update(Student updatedStudent) {
        studentDAO.update(updatedStudent);
    }

    public void delete(Long id){
        studentDAO.delete(id);
    }

    public Set<String> getColumnNames(Class<Student> studentClass) {
        return studentDAO.getColumnNames(studentClass);
    }
}
