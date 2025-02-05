package hospitalApplication.controller;

import hospitalApplication.models.Department;
import hospitalApplication.service.DepartmentService;
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
            @PathVariable Long departmentId,
            @RequestBody Map<String, String> request) {
        Long capoRepartoId = Long.parseLong(request.get("headId"));
        departmentService.assignHeadOfDepartment(departmentId, capoRepartoId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/update/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}

