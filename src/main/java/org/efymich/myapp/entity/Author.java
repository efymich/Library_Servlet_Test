package org.efymich.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.efymich.myapp.converter.NationalityConverter;
import org.efymich.myapp.enums.Nationalities;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Entity
@Table(name = "authors")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authors_seq")
    @SequenceGenerator(name = "authors_seq", sequenceName = "AUTHORS_SEQ",allocationSize = 1)
    @Column(name = "author_id")
    Long authorId;

    @ToString.Exclude
    @Column(name = "author_name")
    String authorName;

    @ToString.Exclude
    @Convert(converter = NationalityConverter.class)
    Nationalities nationality;

    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    Set<Book> books;
}
