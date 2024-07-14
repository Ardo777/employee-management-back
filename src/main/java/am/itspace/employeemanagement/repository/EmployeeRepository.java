package am.itspace.employeemanagement.repository;

import am.itspace.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> getEmployeesByCompanyId(int companyId);
    void deleteEmployeesByCompanyId(int id);
}
