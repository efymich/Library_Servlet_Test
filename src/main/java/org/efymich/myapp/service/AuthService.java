package org.efymich.myapp.service;

import lombok.AllArgsConstructor;
import org.efymich.myapp.dao.StudentDAO;
import org.efymich.myapp.dto.ValidationStudentDTO;
import org.efymich.myapp.entity.Student;
import org.efymich.myapp.utils.PasswordUtils;

@AllArgsConstructor
public class AuthService {
    private StudentDAO studentDAO;

    public ValidationStudentDTO checkPassword(Student inputStudent) {
        Student outputStudent = studentDAO.getByName(inputStudent.getStudentName());

        if (outputStudent == null) {
            return ValidationStudentDTO.builder()
                    .valid(false)
                    .message("There is no such student")
                    .build();
        }

        boolean flag = PasswordUtils.verifyPassword(inputStudent.getPassword(), outputStudent.getPassword());

        if (!flag) {
            return ValidationStudentDTO.builder()
                    .valid(false)
                    .message("Wrong password")
                    .build();
        } else {
            return ValidationStudentDTO.builder()
                    .valid(true)
                    .student(outputStudent)
                    .build();
        }
    }
}
