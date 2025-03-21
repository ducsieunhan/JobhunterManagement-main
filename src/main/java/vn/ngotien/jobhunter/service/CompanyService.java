package vn.ngotien.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.ngotien.jobhunter.domain.Company;
import vn.ngotien.jobhunter.domain.User;
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

  public Company updateCompany(Company company) {
    Company c = getCompanyById(company.getId());
    if (c != null) {
      c.setName(company.getName());
      c.setDescription(company.getDescription());
      c.setAddress(company.getAddress());
      c.setLogo(company.getLogo());
      return this.companyRepository.save(company);
    }
    return null;
  }

  public void deleteCompany(long id) {
    this.companyRepository.deleteById(id);
  }

  public List<Company> getCompanies() {
    return this.companyRepository.findAll();
  }

  public Company getCompanyById(long id) {
    Optional<Company> companyOptional = this.companyRepository.findById(id);
    if (companyOptional.isPresent()) {
      return companyOptional.get();
    }
    return null;
  }

}
