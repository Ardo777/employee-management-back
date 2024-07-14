package am.itspace.employeemanagement.service.impl;

import am.itspace.employeemanagement.entity.Employee;
import am.itspace.employeemanagement.entity.QEmployee;
import am.itspace.employeemanagement.exception.ResourceNotFoundException;
import am.itspace.employeemanagement.repository.EmployeeRepository;
import am.itspace.employeemanagement.service.EmployeeService;
import am.itspace.employeemanagement.util.FileUtil;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static am.itspace.employeemanagement.entity.QEmployee.employee;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EntityManager entityManager;
    private final FileUtil fileUtil;


    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @SneakyThrows
    @Override
    public Employee create(Employee employee, MultipartFile picture) {
        fileUtil.uploadImage(employee, picture);
        return employeeRepository.save(employee);
    }


    @Override
    public Employee getById(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id " + id));
    }

    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getEmployeesByCompanyId(int id) {
        return employeeRepository.getEmployeesByCompanyId(id);
    }

    @Override
    public void deleteByCompanyId(int id) {
        employeeRepository.deleteEmployeesByCompanyId(id);
    }

    @Override
    public List<Employee> searchByName(String keyword) {
        String name = null;
        String surname = null;
        if (keyword.contains(" ")) {
            String[] keywords = keyword.split("\\s+");
            name = keywords[0];
            surname = keywords[1];
        }

        log.debug("Filtering employees by keyword: {}", keyword);

        JPAQuery<Employee> query = new JPAQuery<>(entityManager);
        QEmployee qEmployee = employee;
        JPAQueryBase<Employee, JPAQuery<Employee>> from = query.from(qEmployee);
        List<Employee> fetch;
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(surname)) {
            from.where(qEmployee.name.startsWith(name).and(qEmployee.surname.startsWith(surname)));
            fetch = query.fetch();
        } else {
            from.where(qEmployee.name.startsWith(keyword).or(qEmployee.surname.startsWith((keyword))));
            fetch = query.fetch();
        }
        return fetch;
    }


    @Override
    public Employee updateEmployee(int id, Employee employee, MultipartFile picture) {
        Employee serviceEmployee = getById(id);
        fileUtil.uploadImage(serviceEmployee, picture);
        serviceEmployee.setName(employee.getName());
        serviceEmployee.setSurname(employee.getSurname());
        serviceEmployee.setSalary(employee.getSalary());
        serviceEmployee.setCompany(employee.getCompany());
        employeeRepository.save(serviceEmployee);
        return serviceEmployee;
    }

}
