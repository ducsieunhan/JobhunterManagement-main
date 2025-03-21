package vn.ngotien.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ngotien.jobhunter.domain.Company;
import vn.ngotien.jobhunter.service.CompanyService;
import vn.ngotien.jobhunter.util.error.IdInvalidException;

@RestController
public class CompanyController {

  private CompanyService companyService;

  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @PostMapping("/companies")
  public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company newCompany) {
    Company company = this.companyService.createNewCompany(newCompany);
    return ResponseEntity.status(HttpStatus.CREATED).body(company);
  }

  @GetMapping("/companies")
  public ResponseEntity<List<Company>> getAllCompany() {
    return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getCompanies());
  }

  @PutMapping("/companies")
  public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
    this.companyService.updateCompany(company);
    return ResponseEntity.status(HttpStatus.OK).body(company);
  }

  @DeleteMapping("/companies/{id}")
  public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
    if (id > 150) {
      throw new IdInvalidException("this id is not valid");
    }
    this.companyService.deleteCompany(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

}
