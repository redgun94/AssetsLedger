package com.avocadotech.assetsledger.company;

import com.avocadotech.assetsledger.common.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "company")

@Getter
@Setter
@NoArgsConstructor
public class Company {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@Column(name = "company_nmls",nullable = false,unique = true)
private String companyNmls;

@Column(name = "company_name", nullable = false)
private String name;

@Enumerated(EnumType.STRING)
@Column(name = "company_type",nullable = false)
private CompanyType companyType;

    @Embedded
        private Address address;
}


