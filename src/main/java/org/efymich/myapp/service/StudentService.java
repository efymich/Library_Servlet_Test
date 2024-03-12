package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.StudentDAO;
import org.efymich.myapp.entity.Student;

import java.util.List;

@AllArgsConstructor
public class StudentService {
    private StudentDAO studentDAO;

    public List<Student> getAll() {
        return studentDAO.getAll();
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
}
