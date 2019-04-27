package org.katheer.controller;

import org.katheer.dto.Student;
import org.katheer.event.create.StudentCreateEventPublisher;
import org.katheer.event.delete.StudentDeleteEventPublisher;
import org.katheer.event.search.StudentSearchEventPublisher;
import org.katheer.event.update.StudentUpdateEventPublisher;
import org.katheer.service.StudentService;
import org.katheer.test.Client;
import org.katheer.validator.StudentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("studentController")
public class StudentControllerImpl implements StudentController {

    static {
        System.out.println("StudentControllerImpl class loading...");
    }

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentValidator studentValidator;
    private BufferedReader br;

    public StudentControllerImpl() {
        System.out.println("StudentControllerImpl class instantiating...");
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    private boolean validateStudent(Student student) {
        Map<String, String> error = new HashMap<String, String>();
        MapBindingResult errors = new MapBindingResult(error, "org.katheer" +
                ".dto.Student");
        studentValidator.validate(student, errors);
        int errorCount = errors.getErrorCount();

        if(errorCount == 0) {
            return true;
        }

        List<ObjectError> allErrors = errors.getAllErrors();
        for(ObjectError error1 : allErrors) {
            System.out.println(error1.getDefaultMessage());
        }
        return false;
    }

    private static void printData(Student... students) {
        StudentSearchEventPublisher eventPublisher =
                (StudentSearchEventPublisher) Client.context.getBean(
                        "searchEventPublisher");
        System.out.println("***********************************************STUDENT " +
                "DETAILS**********************************************");
        System.out.println(String.format("%-5s%-32s%-12s%-5s%-42s%-12s", "ID", "Student Name",
                "Dept", "CGPA", "Email", "Mobile"));
        if(students == null || students[0] == null ) {
            System.out.println("                                         " +
                    " *****NO DATA FOUND*****                             " +
                    "             ");
            eventPublisher.publish("Student Search Failed. Status Code : " +
                    "norecordfound");
        } else {
            for (Student s : students) {
                System.out.println(String.format("%-5s%-32s%-12s%-5s%-42s%-12s",
                        String.valueOf(s.getId()), s.getName(), s.getDept(),
                        String.valueOf(s.getCgpa()), s.getEmail(), s.getMobile()));
            }
            eventPublisher.publish("Student Search Succeed");
        }
        System.out.println("***************************************************" +
                "THE END**************************************************");
        System.out.println();
    }

    private Student getStudentDetails() throws Exception {
        System.out.println("Enter Student Details:");
        System.out.print("Student Name    : ");
        String name = br.readLine().trim();
        System.out.print("Student ID      : ");
        int id = Integer.parseInt(br.readLine());
        System.out.print("Student Dept    : ");
        String dept = br.readLine().trim();
        System.out.print("Student CGPA    : ");
        float cgpa = Float.parseFloat(br.readLine());
        System.out.print("Student Email   : ");
        String email = br.readLine().trim();
        System.out.print("Student Mobile  : ");
        String mobile = br.readLine().trim();

        Student student = new Student();
        student.setName(name);
        student.setCgpa(cgpa);
        student.setDept(dept);
        student.setEmail(email);
        student.setId(id);
        student.setMobile(mobile);
        return student;
    }

    @Override
    public void addStudent() {
        StudentCreateEventPublisher eventPublisher = (StudentCreateEventPublisher)
                Client.context.getBean("createEventPublisher");
        try {
            System.out.println("***ADD STUDENT***");
            Student student = this.getStudentDetails();
            String status;

            if(validateStudent(student)) {
                status = studentService.addStudent(student);
            } else {
                status = "failed";
            }

            if (status.equals("success")) {
                System.out.println("Student Inserted Successfully...!");
                eventPublisher.publish("Student Insertion Succeed");
            } else if (status.equals("exists")) {
                System.out.println("Student already exists...!");
                eventPublisher.publish("Student Insertion failed. Status Code" +
                        " : exists");
            } else if (status.equals("failed")) {
                System.out.println("Student Insertion failed...!");
                eventPublisher.publish("Student Insertion Failed. Status Code" +
                        " : other");
            }
        } catch (Exception e) {
            System.out.println("***Exception in getting student details***");
            e.printStackTrace();
        }
    }

    @Override
    public void searchStudent() {
        while (true) {
            try {
                System.out.println("***SEARCH STUDENT***");
                System.out.println("1.Search by ID");
                System.out.println("2.Search by Name");
                System.out.println("3.Search by Department");
                System.out.println("4.Exit");
                System.out.print("Enter choice from above [Options are 1,2," +
                        "3,4] : ");
                int choice = Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1:
                        System.out.print("Enter Student ID : ");
                        int id = Integer.parseInt(br.readLine());
                        printData(this.studentService.searchById(id));
                        break;
                    case 2:
                        System.out.print("Enter Student Name : ");
                        String name = br.readLine().trim();
                        printData(this.studentService.searchByName(name));
                        break;
                    case 3:
                        System.out.print("Enter Student Department : ");
                        String dept = br.readLine().trim();
                        printData(this.studentService.searchByDept(dept));
                        break;
                    case 4:
                        System.out.println("***THANKS FOR USING SEARCH " +
                                "MENU***");
                        return;
                    default:
                        System.out.println("Wrong Choice! Enter Again!!\n");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateStudent() {
        StudentUpdateEventPublisher eventPublisher =
                (StudentUpdateEventPublisher) Client.context.getBean(
                        "updateEventPublisher");
        try {
            int id;
            String status = "";
            System.out.println("***UPDATE STUDENT***");
            System.out.print("Enter Student ID : ");
            id = Integer.parseInt(br.readLine());
            Student student = studentService.searchById(id);

            if(student == null) {
                status = "notexists";
            } else {
                System.out.print(String.format("%-50s : ",
                        "Student Name [ " + student.getName() + " ]") );
                String name = br.readLine().trim();
                System.out.print(String.format("%-50s : ",
                        "Student Dept [ " + student.getDept() + " ]"));
                String dept = br.readLine().trim();
                System.out.print(String.format("%-50s : ",
                        "Student CGPA [ " + student.getCgpa() + " ]"));
                float cgpa = Float.parseFloat(br.readLine().trim());
                System.out.print(String.format("%-50s : ",
                        "Student Email [ " + student.getEmail() + " ]"));
                String email = br.readLine().trim();
                System.out.print(String.format("%-50s : ",
                        "Student Mobile [ " + student.getMobile() + " ]"));
                String mobile = br.readLine().trim();

                student.setName(name);
                student.setDept(dept);
                student.setCgpa(cgpa);
                student.setEmail(email);
                student.setMobile(mobile);


                if(validateStudent(student)) {
                    status = studentService.updateStudent(student);
                } else {
                    status = "failed";
                }
            }


            if (status.equals("success")) {
                System.out.println("Student Updated Successfully...!\n");
                eventPublisher.publish("Student Update Succeed");
            } else if (status.equals("notexists")) {
                System.out.println("Student not exists...!\n");
                eventPublisher.publish("Student Update Failed. Status Code : " +
                        "notexists");
            } else if (status.equals("failed")) {
                System.out.println("Student update failed...!\n");
                eventPublisher.publish("Student Update Failed. Status Code : " +
                        "other");
            }
        } catch (Exception e) {
            System.out.println("***Exception in getting student details***");
            e.printStackTrace();
        }
    }

    @Override
    public void removeStudent() {
        StudentDeleteEventPublisher eventPublisher =
                (StudentDeleteEventPublisher) Client.context.getBean(
                        "deleteEventPublisher");
        try {
            System.out.println("***REMOVE STUDENT***");
            System.out.print("Enter Student ID : ");
            int id = Integer.parseInt(br.readLine());
            String status = studentService.removeStudent(id);

            if (status.equals("success")) {
                System.out.println("Student Removed successfully..!");
                eventPublisher.publish("Student Delete Succeed");
            } else if (status.equals("notexists")) {
                System.out.println("No record found...!");
                eventPublisher.publish("Student Delete Failed. Status Code : " +
                        "notexists");
            } else {
                System.out.println("Student Removal Failed..!");
                eventPublisher.publish("Student Delete Failed. Status Code : " +
                        "other");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
