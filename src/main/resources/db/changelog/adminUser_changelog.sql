-- liquibase formatted sql

--changeset data:Library_2024_adminUser_001 dbms:oracle
Insert INTO STUDENTS(STUDENT_ID, STUDENT_NAME, ROLE, PASSWORD)
VALUES (STUDENTS_SEQ.nextval,'ADMIN','ADMIN','$2a$12$nvjVUU25lQP0EGYZMjud5uStSH9U3KnVYk4GgqX5MPYEwIQqKCzmK');
