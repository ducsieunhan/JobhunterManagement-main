package vn.ngotien.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ngotien.jobhunter.domain.Company;
import vn.ngotien.jobhunter.service.CompanyService;

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

}
