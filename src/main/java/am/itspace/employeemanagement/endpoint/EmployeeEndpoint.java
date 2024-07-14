package am.itspace.employeemanagement.endpoint;

import am.itspace.employeemanagement.entity.Employee;
import am.itspace.employeemanagement.service.EmployeeService;
import am.itspace.employeemanagement.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeEndpoint {

    private final EmployeeService employeeService;
    private final FileUtil fileUtil;


    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping(value = "/createEmployee", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createEmployee(@RequestPart("employee") Employee employee, @RequestPart("picture") MultipartFile picture) {
        employeeService.create(employee, picture);
        return ResponseEntity.ok("Employee created successfully");
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @PutMapping(value = "/updateEmployee/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestPart("employee") Employee employee, @RequestPart(value = "picture", required = false) MultipartFile picture) {
        return ResponseEntity.ok(  employeeService.updateEmployee(id, employee, picture));
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable int id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees-company/{id}")
    public ResponseEntity<List<Employee>> getEmployeeById(@PathVariable int id) {
        List<Employee> employeesByCompanyId = employeeService.getEmployeesByCompanyId(id);
        return ResponseEntity.ok(employeesByCompanyId);
    }

    @GetMapping("/employees/search/{keyword}")
    public ResponseEntity<List<Employee>> searchEmployee(@PathVariable String keyword) {
        return ResponseEntity.ok(employeeService.searchByName(keyword));
    }

    @GetMapping("/employee/picture/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageId) {
        byte[] picture = fileUtil.getPicture(imageId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picture);
    }
}
