package vn.ngotien.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ngotien.jobhunter.domain.Company;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.dto.Meta;
import vn.ngotien.jobhunter.domain.dto.ResultPaginationDTO;
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

  public ResultPaginationDTO getCompanies(Specification<Company> spec, Pageable pageable) {
    Page<Company> page = this.companyRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    Meta meta = new Meta();

    meta.setPage(page.getNumber() + 1);
    meta.setPageSize(page.getSize());

    meta.setPages(page.getTotalPages());
    meta.setTotal(page.getTotalElements());

    rs.setMeta(meta);
    rs.setResult(page.getContent());

    return rs;
  }

  public Company getCompanyById(long id) {
    Optional<Company> companyOptional = this.companyRepository.findById(id);
    if (companyOptional.isPresent()) {
      return companyOptional.get();
    }
    return null;
  }

}
