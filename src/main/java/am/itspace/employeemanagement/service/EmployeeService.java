package am.itspace.employeemanagement.service;

import am.itspace.employeemanagement.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee create(Employee employee, MultipartFile picName);

    Employee getById(int id);

    void deleteById(int id);

    List<Employee> getEmployeesByCompanyId(int id);

    void deleteByCompanyId(int id);

    List<Employee> searchByName(String name);

    Employee updateEmployee(int id,Employee employee, MultipartFile picture);

}
