package com.example.hackathon.controller;

import com.example.hackathon.model.Employee;
import com.example.hackathon.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping({"", "/", "/list"})
    public String list(Model model,
                       @RequestParam(name = "keyword", required = false) String keyword) {
        List<Employee> employees;
        if (keyword != null && !keyword.trim().isEmpty()) {
            employees = employeeService.search(keyword);
        } else {
            employees = employeeService.findAll();
        }
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }
        return "create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "create";
        }
        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("successMsg", "Thêm nhân viên thành công!");
        return "redirect:/employees/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Optional<Employee> emp = employeeService.findById(id);
        if (emp.isEmpty()) {
            return "redirect:/employees/list";
        }
        model.addAttribute("employee", emp.get());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id,
                       @Valid @ModelAttribute("employee") Employee employee,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            employee.setId(id);
            model.addAttribute("employee", employee);
            return "edit";
        }
        employee.setId(id);
        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("successMsg", "Cập nhật nhân viên thành công!");
        return "redirect:/employees/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        boolean deleted = employeeService.deleteById(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMsg", "Xóa nhân viên thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy nhân viên!");
        }
        return "redirect:/employees/list";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/employees/list";
    }
}
