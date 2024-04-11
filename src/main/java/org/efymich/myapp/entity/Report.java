package org.efymich.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reports_seq")
    @SequenceGenerator(name = "reports_seq", sequenceName = "REPORTS_SEQ",allocationSize = 1)
    @Column(name = "rental_id")
    Long rentalId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    Student student;

    @ToString.Exclude
    @Column(name = "rental_date")
    LocalDateTime rentalDate;

    @ToString.Exclude
    @Column(name = "return_date")
    LocalDateTime returnDate;
}
