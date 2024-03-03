package org.efymich.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reports_seq")
    @SequenceGenerator(name = "reports_seq", sequenceName = "REPORTS_SEQ")
    @Column(name = "rental_id")
    Long rentalId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "student_id")
    Student student;

    @Column(name = "rental_date")
    LocalDate rentalDate;

    @Column(name = "return_date")
    LocalDate returnDate;
}
