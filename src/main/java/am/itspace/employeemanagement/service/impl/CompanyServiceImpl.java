package am.itspace.employeemanagement.service.impl;

import am.itspace.employeemanagement.entity.Company;
import am.itspace.employeemanagement.exception.ResourceNotFoundException;
import am.itspace.employeemanagement.repository.CompanyRepository;
import am.itspace.employeemanagement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;


    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(int id) {
        Optional<Company> companyRepositoryById = companyRepository.findById(id);
        companyRepositoryById.orElseThrow(() -> new ResourceNotFoundException("Company does not exist with id " + id));
        return companyRepositoryById.orElse(null);
    }
    @Override
    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public void delete(int id) {
        companyRepository.deleteById(id);
    }
}
