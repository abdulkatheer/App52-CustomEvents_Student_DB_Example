package org.katheer.validator;

import org.katheer.dto.Student;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Properties;
import java.util.regex.Pattern;

public class StudentValidator implements Validator {
    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try{
            Properties errorMessages =
                    PropertiesLoaderUtils.loadProperties(resource);
            Student student = (Student) target;

            if(student.getName() == null || student.getName().equals("")) {
                errors.rejectValue("name", "error.name.required",
                        errorMessages.getProperty("error.name.required"));
            } else {
                Pattern pattern = Pattern
                        .compile("^[a-zA-Z\\s]{4,30}$");
                if(!pattern.matcher(student.getName()).matches()) {
                    errors.rejectValue("name", "error.name.invalid",
                            errorMessages.getProperty("error.name.invalid"));
                }
            }

            if(student.getDept() == null || student.getDept().equals("")) {
                errors.rejectValue("dept", "error.dept.required",
                        errorMessages.getProperty("error.dept.required"));
            } else {
                Pattern pattern = Pattern.compile("(IT|CSE|MECH|ECE|CIVIL" +
                        "|EEE){1}");
                if(!pattern.matcher(student.getDept()).matches()) {
                    errors.rejectValue("dept", "error.dept.invalid",
                            errorMessages.getProperty("error.dept.invalid"));
                }
            }

            if(student.getCgpa() == 0) {
                errors.rejectValue("cgpa", "error.cgpa.required",
                        errorMessages.getProperty("error.cgpa.required"));
            } else if(student.getCgpa() < 0 || student.getCgpa() > 10) {
                errors.rejectValue("cgpa", "error.cgpa.invalid",
                        errorMessages.getProperty("error.cgpa.invalid"));
            }

            if(student.getEmail() == null || student.getEmail().equals("")) {
                errors.rejectValue("email", "error.email.required",
                        errorMessages.getProperty("error.email.required"));
            } else {
                Pattern pattern = Pattern.compile("[(\\w+@[a-zA-Z]+?\\" +
                        ".[a-zA-Z]{2,6})]{4,40}$");
                if(!pattern.matcher(student.getEmail()).matches()) {
                    errors.rejectValue("email", "error.email.invalid",
                            errorMessages.getProperty("error.email.invalid"));
                }
            }

            if(student.getMobile() == null || student.getMobile().equals("")) {
                errors.rejectValue("mobile", "error.mobile.required",
                        errorMessages.getProperty("error.mobile.required"));
            } else {
                Pattern pattern = Pattern.compile("^[6-9]\\d{9}$");
                if(!pattern.matcher(student.getMobile()).matches()) {
                    errors.rejectValue("mobile", "error.mobile.invalid",
                            errorMessages.getProperty("error.mobile.invalid"));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
