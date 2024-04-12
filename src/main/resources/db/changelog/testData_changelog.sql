-- liquibase formatted sql

--changeset data:Library_2024_testdata_001 dbms:oracle runOnChange:true endDelimiter:\nGO
Begin
    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'William Shakespeare', 'English');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Romeo and Juliet', AUTHORS_SEQ.currval, 'Tragedy', 1597);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Jane Austen', 'English');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Pride and Prejudice', AUTHORS_SEQ.currval, 'Romance', 1813);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Leo Tolstoy', 'Russian');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'War and Peace', AUTHORS_SEQ.currval, 'Historical Fiction', 1869);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Fyodor Dostoevsky', 'Russian');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Crime and Punishment', AUTHORS_SEQ.currval, 'Psychological Fiction', 1866);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Charles Dickens', 'English');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Great Expectations', AUTHORS_SEQ.currval, 'Novel', 1861);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Mark Twain', 'American');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'The Adventures of Huckleberry Finn', AUTHORS_SEQ.currval, 'Adventure', 1884);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Emily Bronte', 'English');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Wuthering Heights', AUTHORS_SEQ.currval, 'Gothic Fiction', 1847);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Charlotte Bronte', 'English');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Jane Eyre', AUTHORS_SEQ.currval, 'Gothic Romance', 1847);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Herman Melville', 'American');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'Moby-Dick', AUTHORS_SEQ.currval, 'Adventure', 1851);

    insert into AUTHORS (AUTHOR_ID, AUTHOR_NAME, NATIONALITY) values (AUTHORS_SEQ.nextval, 'Arthur Conan Doyle', 'English');
    insert into BOOKS (BOOK_ID, TITLE, AUTHOR_ID, GENRE, PUBLICATION_YEAR) values (BOOKS_SEQ.nextval, 'The Adventures of Sherlock Holmes', AUTHORS_SEQ.currval, 'Detective Fiction', 1892);
end;
GO
