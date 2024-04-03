package org.efymich.myapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.efymich.myapp.entity.Student;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationStudentDTO {
    boolean valid;
    String message;
    Student student;
}
