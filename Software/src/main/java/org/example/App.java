package org.example;

import java.awt.*;
import java.sql.*;
import java.util.*;

import static org.example.Instructor.input;

public class App {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = ConnectionUtil.getConnection();
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            Scanner scanner = new Scanner(System.in);
            int choice = 0;

            String email = "e";
            String role = "";

            try {
                // Preparing the select statement
                String selectQuery = "SELECT email,role FROM users WHERE token IS NOT NULL";
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);

                // Executing the select statement
                ResultSet rs = pstmt.executeQuery();

                // Iterating over the result set and printing the usernames
                if (rs.next()) {
                    email = rs.getString("email");
                    role = rs.getString("role");
                    System.out.println(email);
                    System.out.println(role);
                }

                // Closing the result set, statement, and database connection
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }

            if (email.equals("e")) {
                System.out.println("Welcome to IIT Ropar management system");
                System.out.println("1. Academics");
                System.out.println("2. Students");
                System.out.println("3. Instructors");
                System.out.print("Enter your choice: ");

                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input type. Please enter an integer.");
                    scanner.nextLine(); // clear the scanner buffer
                }

                switch (choice) {
                    case 1:

                        Scanner input = new Scanner(System.in);

                        System.out.println(" ---------  LOGIN   --------- ");
                        System.out.print("Enter Username : ");
                        String aemail = input.nextLine();
                        System.out.print("Enter Password : ");
                        String apassword = input.nextLine();

                        if (Login.Login_val(aemail, apassword, "academic")) {
                            Currentuser currentUser = new Currentuser(); // create an instance of Currentuser
                            currentUser.setEmail(aemail); // call setEmail on the instance
                            int academicsChoice = 0;

                            System.out.println("Academics Menu");
                            System.out.println("1. Insert a course into the course_catalog.");
                            System.out.println("2. Delete a course from course_catalog.");
                            System.out.println("3. Show All courses");
                            System.out.println("4. Start semester");
                            System.out.println("5. End Semester");
                            System.out.println("6. View grades of Students.");
                            System.out.println("7. Graduation check for student");
                            System.out.println("8. Generate student Transcript");
                            System.out.println("9. Upload Curriculum");
                            System.out.println("10. Upload students and instructor from csv");
                            System.out.println("11. Logout.");

                            try {
                                System.out.print("Enter your choice: ");
                                academicsChoice = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input type. Please enter an integer.");
                                scanner.nextLine(); // clear the scanner buffer
                            }

                            switch (academicsChoice) {
                                case 1:

                                    System.out.print("Enter the course ID: ");
                                    int course_id = input.nextInt();
                                    input.nextLine(); // Consume the new line character

                                    System.out.print("Enter the course name: ");
                                    String course_name = input.nextLine();

                                    System.out.print("Enter the credit structure: ");
                                    String credit_structure = input.nextLine();

                                    Academics.Insert_course(academicsChoice, course_id, course_name,
                                            credit_structure);

                                    App.main(args);

                                    break;
                                case 2:
                                    System.out.println("You chose to delete a course from the course_catalog.");
                                    System.out.print("Enter Course ID: ");
                                    int course_id2 = scanner.nextInt(); // use scanner.next() instead of
                                    // scanner.nextLine()
                                    Academics.Delete_course(course_id2);

                                    App.main(args);

                                    // perform delete course from course_catalog
                                    break;
                                case 3:
                                    System.out.println("You chose to show all courses.");

                                    Academics.Show_course();

                                    App.main(args);
                                    // perform show all courses
                                    break;
                                case 4:
                                    System.out.println("You chose to start the semester.");
                                    Academics.start_sem();
                                    App.main(args);
                                    // perform start semester
                                    break;
                                case 5:
                                    System.out.println("You chose to end the semester.");

                                    Academics.end_sem();
                                    App.main(args);
                                    // perform end semester
                                    break;
                                case 6:
                                    System.out.println("You chose to view the grades of students.");
                                    System.out.print("Enter the email of the student: ");
                                    String email2 = scanner.next(); // use scanner.next() instead of scanner.nextLine()

                                    Academics.view_grad(email2);

                                    App.main(args);
                                    // perform view grades of students
                                    break;
                                case 7:
                                    System.out.print("Enter the Email: ");
                                    email = scanner.next();
                                    Academics.gradcheck(email);
                                    break;
                                case 8:
                                    System.out.print("Enter the Email: ");
                                    email = scanner.next();
                                    Student.TranscriptGenerator(email);
                                    break;
                                case 9:
                                    System.out.println("Curriculum uploader ...");
                                    CurriculumUploader.uploadCurriculum();
                                    App.main(args);
                                case 10:
                                    Csvtodb.fun();
                                    App.main(args);
                                case 11:
                                    // create an object of MainMenu class
                                    MainMenu mainMenu = new MainMenu();
                                    // invoke the logout() method on the object
                                    mainMenu.logout();
                                    App.main(args);
                                    break;
                                default:
                                    System.out.println("Invalid choice!");
                                    App.main(args);
                            }

                            App.main(args);
                        } else {
                            System.out.println("Wrong Username and Password\nTry Again...");
                            App.main(null);
                        }
                        input.close();
                        // Academics.fun(loggedIn);
                        break;
                    case 2:
                        String sql = "SELECT sem_start FROM admin WHERE email = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, "admin@example.com");

                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                boolean semStart = rs.getBoolean("sem_start");
                                System.out.println("Semester Start for Admin: " + semStart);
                                if (semStart) {

                                    System.out.println(" ---------  LOGIN   --------- ");
                                    System.out.print("Enter Username : ");
                                    String semail = scanner.next();
                                    System.out.print("Enter Password : ");
                                    String spassword = scanner.next();

                                    if (Login.Login_val(semail, spassword, "student")) {
                                        Currentuser currentUser = new Currentuser(); // create an instance of
                                        // Currentuser
                                        currentUser.setEmail(semail); // call setEmail on the instance
                                        int studentsChoice = 0;

                                        System.out.println("Students Menu");
                                        System.out.println("1. Register for a course");
                                        System.out.println("2. Deregister for a course.");
                                        System.out.println("3. View my grades.");
                                        System.out.println("4. Update Profile");
                                        System.out.println("5. Change Password");
                                        System.out.println("6. Generate My Transcript");
                                        System.out.println("7. Logout.");
                                        try {
                                            System.out.print("Enter your choice: ");
                                            studentsChoice = scanner.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input type. Please enter an integer.");
                                            scanner.nextLine(); // clear the scanner buffer
                                        }

                                        switch (studentsChoice) {
                                            case 1:
                                                System.out.println("Performing course registration...");
                                                System.out.println("Enter Course ID: ");
                                                int course_id = scanner.nextInt();
                                                Currentuser c = new Currentuser();
                                                email = c.getEmail();
                                                Student.Reg_course(course_id, email);
                                                App.main(null);
                                                // perform register for a course
                                                break;
                                            case 2:
                                                System.out.println("Performing course deregistration...");
                                                course_id = scanner.nextInt();
                                                Student.dereg_course(course_id);
                                                App.main(null);
                                                // perform deregister for a course
                                                break;
                                            case 3:
                                                System.out.println("Performing view grades...");
                                                c = new Currentuser();
                                                String email2 = c.getEmail();

                                                Student.view_grade(email2);
                                                App.main(null);
                                                // perform view my grades
                                                break;
                                            case 4:
                                                System.out.println("Performing profile update...");
                                                c = new Currentuser();
                                                email = c.getEmail();
                                                System.out.print("Enter the address: ");
                                                String address = scanner.next();
                                                scanner.nextLine();
                                                System.out.print("Enter the phone: ");
                                                String phone = scanner.nextLine();
                                                Student.update_profile(address, phone, email);

                                                App.main(null);
                                                break;
                                            case 5:
                                                System.out.println("Performing password change...");
                                                System.out.print("Enter new Password: ");
                                                String password = scanner.next();
                                                Student.change_pass(password);
                                                App.main(null);
                                                // perform change password
                                                break;
                                            case 6:
                                                Currentuser cu = new Currentuser();
                                                email = cu.getEmail();
                                                Student.TranscriptGenerator(email);
                                            case 7:
                                                System.out.println("Performing logout...");
                                                System.out.println("Logged out successfully!");
                                                MainMenu mainMenu = new MainMenu();
                                                // invoke the logout() method on the object
                                                mainMenu.logout();
                                                App.main(args);

                                            default:
                                                System.out.println("Invalid choice!");
                                                App.main(null);
                                        }

                                        App.main(args);
                                    } else {
                                        System.out.println("Wrong Username and Password\nTry Again...");
                                        App.main(null);
                                    }

                                } else {
                                    System.out.println("Semester not Started by Admin: " + semStart);
                                    App.main(args);
                                }
                            } else {
                                System.out.println("No data found for admin@example.com");
                            }
                            // Student.fun(loggedIn);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        App.main(args);
                        break;
                    case 3:
                        sql = "SELECT sem_start FROM admin WHERE email = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, "admin@example.com");

                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                boolean semStart = rs.getBoolean("sem_start");
                                System.out.println("Semester Start for Admin: " + semStart);
                                if (semStart) {

                                    System.out.println(" ---------  LOGIN   --------- ");
                                    System.out.print("Enter Username : ");
                                    String iemail = scanner.next();
                                    System.out.print("Enter Password : ");
                                    String ipassword = scanner.next();

                                    if (Login.Login_val(iemail, ipassword, "instructor")) {
                                        Currentuser currentUser = new Currentuser(); // create an instance of
                                        // Currentuser
                                        currentUser.setEmail(iemail); // call setEmail on the instance

                                        int instructorsChoice = 0;

                                        System.out.println("Instructors Menu");
                                        System.out.println("1. Add a grade for a student");
                                        System.out.println("2. View my courses.");
                                        System.out.println("3. Update Profile");
                                        System.out.println("4. Change Password");
                                        System.out.println("5. Upload Grades and compute CGPA");
                                        System.out.println("6. Offer a course.");
                                        System.out.println("7. Generate student Transcript");
                                        System.out.println("8. Logout.");

                                        try {
                                            System.out.print("Enter your choice: ");
                                            instructorsChoice = scanner.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input type. Please enter an integer.");
                                            scanner.nextLine(); // clear the scanner buffer
                                        }

                                        switch (instructorsChoice) {
                                            case 1:
                                                System.out.println("Add a grade for a student");
                                                System.out.print("Enter Student's Email: ");
                                                email = scanner.next();
                                                System.out.print("Enter Student's course id: ");
                                                int course_id = scanner.nextInt();
                                                System.out.print("Enter Student's grade: ");
                                                int grade = scanner.nextInt();

                                                String selectQuery = "SELECT curr_sem FROM students";
                                                rs = stmt.executeQuery(selectQuery);
                                                int curr_sem = 0;
                                                // Process the results
                                                if (rs.next()) {
                                                    curr_sem = rs.getInt("curr_sem");
                                                    System.out.println("Current Semester: " + curr_sem);
                                                }

                                                Instructor.upload_grades(email, course_id, curr_sem, grade);
                                                CGPA.computeCGPA();
                                                App.main(null);
                                                break;
                                            case 2:
                                                System.out.println("View my courses");
                                                Currentuser c = new Currentuser();
                                                email = c.getEmail();
                                                Instructor.view_grad(email);
                                                App.main(null);
                                                break;
                                            case 3:
                                                System.out.println("Update profile");
                                                c = new Currentuser();
                                                email = c.getEmail();
                                                System.out.print("Enter the address: ");
                                                String address = scanner.next();
                                                scanner.nextLine();
                                                System.out.print("Enter the phone: ");
                                                String phone = scanner.nextLine();
                                                Instructor.update_profile(address, phone, email);
                                                App.main(null);
                                                break;
                                            case 4:
                                                System.out.println("Change password");
                                                System.out.print("Enter new Password: ");
                                                String password = scanner.next();
                                                Instructor.change_pass(password);
                                                App.main(null);
                                                break;
                                            case 5:
                                                GradeUpdater.GradeUpdaterfun();
                                                CGPA.computeCGPA();
                                                App.main(args);
                                                break;
                                            case 6:
                                                System.out.print("Enter the course id: ");
                                                course_id = scanner.nextInt();
                                                scanner.nextLine();
                                                System.out.print("Enter the cgpa constraints: ");
                                                int cgpa_con = scanner.nextInt();
                                                c = new Currentuser();
                                                email = c.getEmail();
                                                Instructor.addCourseOffering(course_id,email,cgpa_con);
                                                App.main(args);
                                            case 7:
                                                System.out.print("Enter the Email: ");
                                                email = scanner.next();
                                                Student.TranscriptGenerator(email);
                                                break;
                                            case 8:
                                                // create an object of MainMenu class
                                                MainMenu mainMenu = new MainMenu();
                                                // invoke the logout() method on the object
                                                mainMenu.logout();
                                            default:
                                                System.out.println("Invalid choice!");
                                                App.main(null);
                                        }

                                    } else {
                                        System.out.println("Wrong Username and Password\nTry Again...");
                                        App.main(null);
                                    }

                                } else {
                                    System.out.println("Semester not Started by Admin: " + semStart);
                                    App.main(args);
                                }
                            } else {
                                System.out.println("No data found for admin@example.com");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        App.main(args);
                        // Faculty.fun(loggedIn);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }

            } else {
                if (role.equals("academic")) {
                    int academicsChoice = 0;

                    System.out.println("Academics Menu");
                    System.out.println("1. Insert a course into the course_catalog.");
                    System.out.println("2. Delete a course from course_catalog.");
                    System.out.println("3. Show All courses");
                    System.out.println("4. Start semester");
                    System.out.println("5. End Semester");
                    System.out.println("6. View grades of Students.");
                    System.out.println("7. Graduation check for student");
                    System.out.println("8. Generate student Transcript");
                    System.out.println("9. Upload Curriculum");
                    System.out.println("10. Upload students and instructor from csv");
                    System.out.println("11. Logout.");

                    try {
                        System.out.print("Enter your choice: ");
                        academicsChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input type. Please enter an integer.");
                        scanner.nextLine(); // clear the scanner buffer
                    }

                    switch (academicsChoice) {
                        case 1:

                            System.out.print("Enter the course ID: ");
                            int course_id = input.nextInt();
                            input.nextLine(); // Consume the new line character

                            System.out.print("Enter the course name: ");
                            String course_name = input.nextLine();

                            System.out.print("Enter the credit structure: ");
                            String credit_structure = input.nextLine();

                            Academics.Insert_course(academicsChoice, course_id, course_name,
                                    credit_structure);

                            App.main(args);

                            break;
                        case 2:
                            System.out.println("You chose to delete a course from the course_catalog.");
                            System.out.print("Enter Course ID: ");
                            int course_id2 = scanner.nextInt(); // use scanner.next() instead of
                            // scanner.nextLine()
                            Academics.Delete_course(course_id2);

                            App.main(args);

                            // perform delete course from course_catalog
                            break;
                        case 3:
                            System.out.println("You chose to show all courses.");

                            Academics.Show_course();

                            App.main(args);
                            // perform show all courses
                            break;
                        case 4:
                            System.out.println("You chose to start the semester.");
                            Academics.start_sem();
                            App.main(args);
                            // perform start semester
                            break;
                        case 5:
                            System.out.println("You chose to end the semester.");

                            Academics.end_sem();
                            App.main(args);
                            // perform end semester
                            break;
                        case 6:
                            System.out.println("You chose to view the grades of students.");
                            System.out.print("Enter the email of the student: ");
                            String email2 = scanner.next(); // use scanner.next() instead of scanner.nextLine()

                            Academics.view_grad(email2);

                            App.main(args);
                            // perform view grades of students
                            break;
                        case 7:
                            System.out.print("Enter the Email: ");
                            email = scanner.next();
                            Academics.gradcheck(email);
                            break;
                        case 8:
                            System.out.print("Enter the Email: ");
                            email = scanner.next();
                            Student.TranscriptGenerator(email);
                            break;
                        case 9:
                            System.out.println("Curriculum uploader ...");
                            CurriculumUploader.uploadCurriculum();
                            App.main(args);
                        case 10:
                            Csvtodb.fun();
                            App.main(args);
                        case 11:
                            // create an object of MainMenu class
                            MainMenu mainMenu = new MainMenu();
                            // invoke the logout() method on the object
                            mainMenu.logout();
                            App.main(args);
                            break;
                        default:
                            System.out.println("Invalid choice!");
                            App.main(args);
                    }

                    App.main(args);
                    input.close();

                } else if (role.equals("student")) {
                    int studentsChoice = 0;

                    System.out.println("Students Menu");
                    System.out.println("1. Register for a course");
                    System.out.println("2. Deregister for a course.");
                    System.out.println("3. View my grades.");
                    System.out.println("4. Update Profile");
                    System.out.println("5. Change Password");
                    System.out.println("6. Generate My Transcript");
                    System.out.println("7. Logout.");
                    try {
                        System.out.print("Enter your choice: ");
                        studentsChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input type. Please enter an integer.");
                        scanner.nextLine(); // clear the scanner buffer
                    }

                    switch (studentsChoice) {
                        case 1:
                            System.out.println("Performing course registration...");
                            System.out.println("Enter Course ID: ");
                            int course_id = scanner.nextInt();
                            Currentuser c = new Currentuser();
                            email = c.getEmail();
                            Student.Reg_course(course_id, email);
                            App.main(null);
                            // perform register for a course
                            break;
                        case 2:
                            System.out.println("Performing course deregistration...");
                            course_id = scanner.nextInt();
                            Student.dereg_course(course_id);
                            App.main(null);
                            // perform deregister for a course
                            break;
                        case 3:
                            System.out.println("Performing view grades...");
                            c = new Currentuser();
                            String email2 = c.getEmail();

                            Student.view_grade(email2);
                            App.main(null);
                            // perform view my grades
                            break;
                        case 4:
                            System.out.println("Performing profile update...");
                            c = new Currentuser();
                            email = c.getEmail();
                            System.out.print("Enter the address: ");
                            String address = scanner.next();
                            scanner.nextLine();
                            System.out.print("Enter the phone: ");
                            String phone = scanner.nextLine();
                            Student.update_profile(address, phone, email);

                            App.main(null);
                            break;
                        case 5:
                            System.out.println("Performing password change...");
                            System.out.print("Enter new Password: ");
                            String password = scanner.next();
                            Student.change_pass(password);
                            App.main(null);
                            // perform change password
                            break;
                        case 6:
                            Currentuser cu = new Currentuser();
                            email = cu.getEmail();
                            Student.TranscriptGenerator(email);
                        case 7:
                            System.out.println("Performing logout...");
                            System.out.println("Logged out successfully!");
                            MainMenu mainMenu = new MainMenu();
                            // invoke the logout() method on the object
                            mainMenu.logout();
                            App.main(args);

                        default:
                            System.out.println("Invalid choice!");
                            App.main(null);
                    }

                    App.main(args);

                } else if (role.equals("instructor")) {
                    int instructorsChoice = 0;

                    System.out.println("Instructors Menu");
                    System.out.println("1. Add a grade for a student");
                    System.out.println("2. View my courses.");
                    System.out.println("3. Update Profile");
                    System.out.println("4. Change Password");
                    System.out.println("5. Upload Grades and compute CGPA");
                    System.out.println("6. Offer a course.");
                    System.out.println("7. Generate student Transcript");
                    System.out.println("8. Logout.");

                    try {
                        System.out.print("Enter your choice: ");
                        instructorsChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input type. Please enter an integer.");
                        scanner.nextLine(); // clear the scanner buffer
                    }

                    switch (instructorsChoice) {
                        case 1:
                            System.out.println("Add a grade for a student");
                            System.out.print("Enter Student's Email: ");
                            email = scanner.next();
                            System.out.print("Enter Student's course id: ");
                            int course_id = scanner.nextInt();
                            System.out.print("Enter Student's grade: ");
                            int grade = scanner.nextInt();

                            String selectQuery = "SELECT curr_sem FROM students";
                            ResultSet rs = stmt.executeQuery(selectQuery);
                            int curr_sem = 0;
                            // Process the results
                            if (rs.next()) {
                                curr_sem = rs.getInt("curr_sem");
                                System.out.println("Current Semester: " + curr_sem);
                            }

                            Instructor.upload_grades(email, course_id, curr_sem, grade);
                            CGPA.computeCGPA();
                            App.main(null);
                            break;
                        case 2:
                            System.out.println("View my courses");
                            Currentuser c = new Currentuser();
                            email = c.getEmail();
                            Instructor.view_grad(email);
                            App.main(null);
                            break;
                        case 3:
                            System.out.println("Update profile");
                            c = new Currentuser();
                            email = c.getEmail();
                            System.out.print("Enter the address: ");
                            String address = scanner.next();
                            scanner.nextLine();
                            System.out.print("Enter the phone: ");
                            String phone = scanner.nextLine();
                            Instructor.update_profile(address, phone, email);
                            App.main(null);
                            break;
                        case 4:
                            System.out.println("Change password");
                            System.out.print("Enter new Password: ");
                            String password = scanner.next();
                            Instructor.change_pass(password);
                            App.main(null);
                            break;
                        case 5:
                            GradeUpdater.GradeUpdaterfun();
                            CGPA.computeCGPA();
                            App.main(args);
                            break;
                        case 6:
                            System.out.print("Enter the course id: ");
                            course_id = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Enter the cgpa constraints: ");
                            int cgpa_con = scanner.nextInt();
                            c = new Currentuser();
                            email = c.getEmail();
                            Instructor.addCourseOffering(course_id,email,cgpa_con);
                            App.main(args);
                        case 7:
                            System.out.print("Enter the Email: ");
                            email = scanner.next();
                            Student.TranscriptGenerator(email);
                            break;
                        case 8:
                            // create an object of MainMenu class
                            MainMenu mainMenu = new MainMenu();
                            // invoke the logout() method on the object
                            mainMenu.logout();
                        default:
                            System.out.println("Invalid choice!");
                            App.main(null);
                    }

                }
            }

            scanner.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }

        }

    }

    public static class MainMenu extends Menu implements org.example.MainMenu {
        private boolean isLoggedIn = false;

        // implementation of the Menu interface
        @Override
        public void login() {
            // prompt user for username and password
            Scanner input = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = input.nextLine();
            System.out.print("Enter password: ");
            String password = input.nextLine();
            System.out.print("Enter role: ");
            String role = input.nextLine();

            // validate credentials
            if (Login.Login_val(username, password, role)) {
                System.out.println("Login successful!");
                isLoggedIn = true;
            } else {
                System.out.println("Invalid username or password.");
                isLoggedIn = false;
            }
        }

        @Override
        public void logout() {

            System.out.println("You chose to logout.");
            // reset current user's tokens
            Currentuser currentuser = new Currentuser();
            currentuser.resetTokens();
            // return to main menu


            // other methods...
        }

    }
}

