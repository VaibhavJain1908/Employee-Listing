package com.nagarro.employeelisting.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.employeelisting.models.Employee;
import com.nagarro.employeelisting.services.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    private String userName;
    private ObjectMapper mapper = new ObjectMapper();
    private File jsonFile = new File("employees.json");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CsvService csvService;

    public void writeToFile() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:9090/employees/",
                String.class);

        try {
            FileWriter fw = new FileWriter(jsonFile);
            fw.write(response.getBody());
            fw.close();
        }
        catch (Exception e) {}
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String validate(Map<String, Object> model, @RequestParam String userName, @RequestParam String password) {
        try {
            restTemplate.getForEntity(
                            "http://localhost:9090/login/" + userName + "/" + password,
                            String.class);
        }
        catch (Exception e) {
            model.put("errorMessage", e.getMessage());
            return "login";
        }

        writeToFile();

        try {
            List<Employee> employees = mapper.readValue(jsonFile, List.class);
            model.put("employees", employees);
        }
        catch (Exception e) {
            List<Employee> employees = new ArrayList<>();
            model.put("employees", employees);
        }

        this.userName = userName;
        model.put("user", userName);

        return "employees";
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public String employees(Map<String, Object> model) {

        writeToFile();

        try {
            List<Employee> employees = mapper.readValue(jsonFile, List.class);
            model.put("employees", employees);
        }
        catch (Exception e) {
            List<Employee> employees = new ArrayList<>();
            model.put("employees", employees);
        }

        model.put("user", userName);
        return "employees";
    }

    @RequestMapping(value = "/delete/{employeeCode}", method = RequestMethod.GET)
    public String delete(Map<String, Object> model, @PathVariable Integer employeeCode) {

        restTemplate.getForEntity(
                "http://localhost:9090/delete/" + employeeCode,
                String.class);

        writeToFile();

        try {
            List<Employee> employees = mapper.readValue(jsonFile, List.class);
            model.put("employees", employees);
        }
        catch (Exception e) {
            List<Employee> employees = new ArrayList<>();
            model.put("employees", employees);
        }

        model.put("user", userName);
        return "employees";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Map<String, Object> model) {

        model.put("link", "add");
        model.put("user", userName);
        model.put("button", "Add");
        return "employee";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addEmployee(Map<String, Object> model, @RequestParam String code, @RequestParam String name,
                              @RequestParam String location, @RequestParam String email, @RequestParam String date) {

        restTemplate.getForEntity(
                "http://localhost:9090/add/" + code + "/" + name + "/" +
                        location + "/" + email + "/" + date,
                String.class);

        writeToFile();
        try {
            List<Employee> employees = mapper.readValue(jsonFile, List.class);
            model.put("employees", employees);
        }
        catch (Exception e) {
            List<Employee> employees = new ArrayList<>();
            model.put("employees", employees);
        }

        model.put("user", userName);
        return "employees";
    }

    @RequestMapping(value = "/edit/{employeeCode}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer employeeCode,  Map<String, Object> model) {

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:9090/employee/" + employeeCode,
                String.class);

        try {
            FileWriter fw = new FileWriter(jsonFile);
            fw.write(response.getBody());
            fw.close();
            Employee employee = mapper.readValue(jsonFile, Employee.class);

            model.put("code", employeeCode);
            model.put("name", employee.getName());
            model.put("location", employee.getLocation());
            model.put("email", employee.getEmail());
            model.put("date", employee.getDateOfBirth());
        }
        catch (Exception e) {}

        model.put("link", "edit");
        model.put("user", userName);
        model.put("readOnly", "readonly");
        model.put("button", "Save");
        return "employee";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editEmployee(Map<String, Object> model, @RequestParam Integer code, @RequestParam String name,
                               @RequestParam String location, @RequestParam String email, @RequestParam String date) {

        restTemplate.getForEntity(
                "http://localhost:9090/edit/" + code + "/" + name + "/" +
                        location + "/" + email + "/" + date,
                String.class);

        writeToFile();
        try {
            List<Employee> employees = mapper.readValue(jsonFile, List.class);
            model.put("employees", employees);
        }
        catch (Exception e) {
            List<Employee> employees = new ArrayList<>();
            model.put("employees", employees);
        }

        model.put("user", userName);
        return "employees";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(Map<String, Object> model, HttpServletResponse response) throws IOException {

        writeToFile();
        try {
            List<Employee> employeeList = mapper.readValue(jsonFile, List.class);

            List<Employee> employees = mapper.convertValue(
                    employeeList,
                    new TypeReference<List<Employee>>() { });

            csvService.exportToCSV(response, employees);
        }
        catch (Exception e) {
            List<Employee> employees = new ArrayList<>();
            csvService.exportToCSV(response, employees);
        }

    }
}
