package vn.ngotien.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.ngotien.jobhunter.domain.Company;
import vn.ngotien.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
  private CompanyRepository companyRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public Company createNewCompany(Company company) {
    return this.companyRepository.save(company);
  }
}
