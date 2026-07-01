package com.example.allsyn.controller;

import com.example.allsyn.dto.EmployeeRequestDTO;
import com.example.allsyn.dto.EmployeeResponseDTO;
import com.example.allsyn.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeWebController {

    private final EmployeeService employeeService;

    // List all employees
    @GetMapping
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            Model model) {
        log.info("Listing employees with keyword: {}", keyword);

        List<EmployeeResponseDTO> employees;
        if (keyword != null && !keyword.trim().isEmpty()) {
            employees = employeeService.searchEmployees(keyword);
        } else {
            employees = employeeService.getAllEmployees();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        model.addAttribute("employeeCount", employees.size());

        return "employee/list";
    }

    // Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        log.info("Showing create employee form");
        model.addAttribute("employee", new EmployeeRequestDTO());
        model.addAttribute("pageTitle", "Add New Employee");
        model.addAttribute("action", "/employees/create");
        model.addAttribute("isEdit", false);
        return "employee/form";
    }

    // Create employee
    @PostMapping("/create")
    public String createEmployee(
            @Valid @ModelAttribute("employee") EmployeeRequestDTO employeeRequestDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Employee");
            model.addAttribute("action", "/employees/create");
            model.addAttribute("isEdit", false);
            return "employee/form";
        }

        try {
            EmployeeResponseDTO created = employeeService.createEmployee(employeeRequestDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Employee created successfully: " + created.getFullName());
            return "redirect:/employees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Add New Employee");
            model.addAttribute("action", "/employees/create");
            model.addAttribute("isEdit", false);
            return "employee/form";
        }
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Showing edit form for employee ID: {}", id);

        try {
            EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
            EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .department(employee.getDepartment())
                    .salary(employee.getSalary())
                    .hireDate(employee.getHireDate())
                    .phone(employee.getPhone())
                    .address(employee.getAddress())
                    .position(employee.getPosition())
                    .build();

            model.addAttribute("employee", request);
            model.addAttribute("employeeId", id);
            model.addAttribute("pageTitle", "Edit Employee");
            model.addAttribute("action", "/employees/edit/" + id);
            model.addAttribute("isEdit", true);
            return "employee/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employees";
        }
    }

    // Update employee
    @PostMapping("/edit/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") EmployeeRequestDTO employeeRequestDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeId", id);
            model.addAttribute("pageTitle", "Edit Employee");
            model.addAttribute("action", "/employees/edit/" + id);
            model.addAttribute("isEdit", true);
            return "employee/form";
        }

        try {
            EmployeeResponseDTO updated = employeeService.updateEmployee(id, employeeRequestDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Employee updated successfully: " + updated.getFullName());
            return "redirect:/employees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("employeeId", id);
            model.addAttribute("pageTitle", "Edit Employee");
            model.addAttribute("action", "/employees/edit/" + id);
            model.addAttribute("isEdit", true);
            return "employee/form";
        }
    }

    // View employee details
    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Viewing employee with ID: {}", id);

        try {
            EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employee);
            return "employee/view";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employees";
        }
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Deleting employee with ID: {}", id);

        try {
            EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("success",
                    "Employee deleted successfully: " + employee.getFullName());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/employees";
    }
}