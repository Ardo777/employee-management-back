package am.itspace.employeemanagement.service;

import am.itspace.employeemanagement.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();

    Company getCompanyById(int id);

    Company addCompany(Company company);

    void delete(int id);

}
