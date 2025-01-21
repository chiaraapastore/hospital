package com.example.hospital.controller;

import com.example.hospital.models.Department;
import com.example.hospital.models.HeadOfDepartmentDTO;
import com.example.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {


    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("/create")
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PutMapping("/{departmentId}/assign-head")
    public ResponseEntity<Void> assignHeadOfDepartment(
            @PathVariable String departmentId,
            @RequestBody Map<String, HeadOfDepartmentDTO> request) {
        HeadOfDepartmentDTO capoRepartoId = request.get("capoRepartoId");
        departmentService.assignHeadOfDepartment(departmentId, capoRepartoId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/update/{id}")
    public Department updateDepartment(@PathVariable String id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}

