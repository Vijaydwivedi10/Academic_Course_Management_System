drop database db;
Create database db;
\c db;


CREATE TABLE course_catalog (
  course_id INT PRIMARY KEY NOT NULL,
  course_name VARCHAR(255) NOT NULL,
  credit_structure VARCHAR(255) NOT NULL
);

CREATE TABLE prerequisites (
  course_id INT NOT NULL,
  prerequisite_course_id INT NOT NULL,
  PRIMARY KEY (course_id, prerequisite_course_id),
  FOREIGN KEY (course_id) REFERENCES course_catalog(course_id),
  FOREIGN KEY (prerequisite_course_id) REFERENCES course_catalog(course_id)
);

CREATE TABLE course_curriculum (
  batch INT NOT NULL,
  semester INT NOT NULL,
  department VARCHAR(255) NOT NULL,
  course_id INT NOT NULL,
  course_type VARCHAR(255) NOT NULL,
  PRIMARY KEY (batch, semester, department),
  FOREIGN KEY (course_id) REFERENCES course_catalog(course_id)
);


CREATE TABLE users (
  email VARCHAR(255) PRIMARY KEY NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255),
  phone VARCHAR(255),
  role VARCHAR(255) NOT NULL,
  token VARCHAR(255)
);

CREATE TABLE instructors (
  instructor_id SERIAL PRIMARY KEY,
  department VARCHAR(255) NOT NULL, 
  email VARCHAR(255) UNIQUE NOT NULL,
  joining_date DATE,
  FOREIGN KEY (email) REFERENCES users(email)
);

CREATE TABLE students (
  email VARCHAR(255) NOT NULL UNIQUE,
  entry_number VARCHAR(255) NOT NULL UNIQUE,
  batch INT NOT NULL,
  department VARCHAR(255) NOT NULL,
  curr_sem INT NOT NULL,
  cgpa FLOAT NOT NULL,
  PRIMARY KEY (email, entry_number),
  FOREIGN KEY (email) REFERENCES users(email)
);


CREATE TABLE course_offerings (
  course_id INT NOT NULL,
  instructor_id INT NOT NULL,
  cgpa_constraint FLOAT NOT NULL,
  min_credits INT NOT NULL,
  PRIMARY KEY (course_id, instructor_id),
  FOREIGN KEY (course_id) REFERENCES course_catalog(course_id),
  FOREIGN KEY (instructor_id) REFERENCES instructors(instructor_id)
);

CREATE TABLE grades (
  email VARCHAR(255) NOT NULL,
  course_id INT NOT NULL,
  semester INT NOT NULL,
  grade INT NOT NULL,
  PRIMARY KEY (email, course_id, semester),
  FOREIGN KEY (email) REFERENCES students(email),
  FOREIGN KEY (course_id) REFERENCES course_catalog(course_id)
);

CREATE TABLE admin (
  email VARCHAR(255) PRIMARY KEY NOT NULL,
  sem_start boolean NOT NULL,
  FOREIGN KEY (email) REFERENCES users(email)
);

-- INSERT INTO users (email, password, name, address, phone, role)
-- VALUES ('student@example.com', 'password', 'John Smith', '123 Main St', '555-1234', 'student');

-- INSERT INTO users (email, password, name, address, phone, role)
-- VALUES ('instructor@example.com', 'password', 'Jane Doe', '456 Elm St', '555-5678', 'instructor');

-- INSERT INTO users (email, password, name, address, phone, role)
-- VALUES ('academic@example.com', 'password', 'Sarah Johnson', '789 Oak St', '555-9012', 'academic');

-- INSERT INTO admin (email, sem_start)
-- VALUES ('academic@example.com', false);

-- UPDATE admin SET sem_start = false WHERE email = 'academic@example.com';

-- insert sample data into course_catalog table
INSERT INTO course_catalog (course_id, course_name, credit_structure) VALUES
  (101, 'Intro to Programming', '3-0-3'),
  (102, 'Data Structures', '3-1-3'),
  (103, 'Algorithms', '3-0-3'),
  (104, 'Database Systems', '3-1-3');


-- insert sample data into prerequisites table
INSERT INTO prerequisites (course_id, prerequisite_course_id) VALUES
  (102, 101),
  (103, 102),
  (104, 103);

-- insert sample data into course_curriculum table
INSERT INTO course_curriculum (batch, semester, department, course_id, course_type) VALUES
  (2022, 1, 'CSE', 101, 'program_core'),
  (2022, 2, 'CSE', 102, 'program_core'),
  (2023, 1, 'CSE', 103, 'engineering_core'),
  (2023, 2, 'CSE', 104, 'elective');

-- insert sample data into users table
INSERT INTO users (email, password, name, address, phone, role) VALUES
  ('admin@example.com', 'password', 'Admin User', NULL, NULL, 'academic'),
  ('john@example.com', 'password', 'John Doe', '123 Main St', '555-1234', 'student'),
  ('jane@example.com', 'password', 'Jane Smith', '456 Oak St', '555-5678', 'instructor');
  

-- insert sample data into instructors table
INSERT INTO instructors (department, email, joining_date) VALUES
  ('CSE', 'jane@example.com', '2020-01-01');

-- insert sample data into students table
INSERT INTO students (email, entry_number, batch, department, curr_sem, cgpa) VALUES
  ('john@example.com', '2022001', 2022, 'CSE', 1, 3.5);

-- insert sample data into course_offerings table
INSERT INTO course_offerings (course_id, instructor_id, cgpa_constraint, min_credits) VALUES
  (101, 1, 3.0, 3),
  (102, 1, 3.3, 4),
  (103, 1, 3.5, 3),
  (104, 1, 3.0, 3);

-- insert sample data into grades table
INSERT INTO grades (email, course_id, semester, grade) VALUES
  ('john@example.com', 101, 1, 8),
  ('john@example.com', 102, 2, 9),
  ('john@example.com', 103, 1, 7),
  ('john@example.com', 104, 2, 10);

-- insert sample data into admin table
INSERT INTO admin (email, sem_start) VALUES
  ('admin@example.com', true);
