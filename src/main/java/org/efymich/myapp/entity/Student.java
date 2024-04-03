package org.efymich.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.efymich.myapp.enums.Roles;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_seq")
    @SequenceGenerator(name = "students_seq", sequenceName = "STUDENTS_SEQ",allocationSize = 1)
    @Column(name = "student_id")
    Long studentId;

    @NotEmpty(message = "please put name")
    @Column(name = "student_name")
    String studentName;

    @Enumerated(EnumType.STRING)
    Roles role;

    @NotNull(message = "please put password")
//    @Size(min = 8,max = 20,message = "password should comply with requirements")
    String password;
}
