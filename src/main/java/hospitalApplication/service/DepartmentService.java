package hospitalApplication.service;

import hospitalApplication.config.AuthenticationService;
import hospitalApplication.models.Department;
import hospitalApplication.repository.DepartmentRepository;
import hospitalApplication.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public DepartmentService(DepartmentRepository departmentRepository,
                             UtenteRepository utenteRepository,
                             AuthenticationService authenticationService) {
        this.departmentRepository = departmentRepository;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dipartimento non trovato"));

        department.setNome(updatedDepartment.getNome());
        department.setCapoReparto(updatedDepartment.getCapoReparto());

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
