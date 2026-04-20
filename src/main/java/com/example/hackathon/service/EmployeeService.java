package com.example.hackathon.service;

import com.example.hackathon.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final List<Employee> employees = new ArrayList<>();
    private int nextId = 1;

    public EmployeeService() {
        employees.add(new Employee("NV001", "Nguyễn Văn A", "Developer", 15000000.0, ""));
        employees.add(new Employee("NV002", "Nguyễn Văn B", "Manager", 25000000.0, ""));
        employees.add(new Employee("NV003", "Nguyễn Văn C", "Tester", 12000000.0, ""));
        nextId = 4;
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public Optional<Employee> findById(String id) {
        return employees.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public void save(Employee employee) {
        if (employee.getId() == null || employee.getId().isEmpty()) {
            employee.setId(String.format("NV%03d", nextId++));
            employees.add(employee);
        } else {
            Optional<Employee> existing = findById(employee.getId());
            if (existing.isPresent()) {
                Employee e = existing.get();
                e.setFullName(employee.getFullName());
                e.setPosition(employee.getPosition());
                e.setSalary(employee.getSalary());
                e.setAvatar(employee.getAvatar());
            } else {
                employees.add(employee);
            }
        }
    }

    public boolean deleteById(String id) {
        return employees.removeIf(e -> e.getId().equals(id));
    }

    public List<Employee> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String lower = keyword.toLowerCase().trim();
        return employees.stream()
                .filter(e ->
                        (e.getFullName() != null && e.getFullName().toLowerCase().contains(lower)) ||
                        (e.getPosition() != null && e.getPosition().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    public boolean existsById(String id) {
        return employees.stream().anyMatch(e -> e.getId().equals(id));
    }
}
