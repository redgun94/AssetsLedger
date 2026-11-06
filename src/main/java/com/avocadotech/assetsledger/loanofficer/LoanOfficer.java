package com.avocadotech.assetsledger.loanofficer;
import com.avocadotech.assetsledger.company.Company;
import com.avocadotech.assetsledger.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_officers")

@Getter
@Setter
@NoArgsConstructor
public class LoanOfficer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;


    @Column(name = "nmls", unique = true)
    private String nmls;

    @Column(name = "deleted_nmls")
    private String deletedNmls;

    @Enumerated(EnumType.STRING)
    @Column(name = "officer_type", nullable = false)
    private OfficerType officerType;
}
