package am.itspace.employeemanagement.endpoint;

import am.itspace.employeemanagement.entity.Company;
import am.itspace.employeemanagement.service.CompanyService;
import am.itspace.employeemanagement.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyEndpoint {

    private final CompanyService companyService;
    private final EmployeeService employeeService;

    @PostMapping("/createCompany")
    public ResponseEntity<Company> addCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.addCompany(company));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") int id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PutMapping("/updateCompany/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable("id") int id, @RequestBody Company company) {
        Company companyById = companyService.getCompanyById(id);
        companyById.setName(company.getName());
        companyById.setAddress(company.getAddress());
        companyById.setPhone(company.getPhone());
        companyById.setEmail(company.getEmail());
        companyService.addCompany(companyById);
        return ResponseEntity.ok(companyById);
    }

    @DeleteMapping("/deleteCompany/{id}")
    @Transactional
    public ResponseEntity<Company> deleteCompany(@PathVariable("id") int id) {
        employeeService.deleteByCompanyId(id);
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
