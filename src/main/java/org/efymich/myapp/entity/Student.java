package org.efymich.myapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.efymich.myapp.enums.Roles;

import java.util.List;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Builder
@Data
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

    @NotEmpty(message = "please put password")
    @Size(min = 6,message = "password should comply with requirements")
    String password;

    @OneToMany(mappedBy = "student")
    List<Report> reports;
}
