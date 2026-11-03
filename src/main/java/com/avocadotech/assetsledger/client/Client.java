package com.avocadotech.assetsledger.client;

import com.avocadotech.assetsledger.loanofficer.LoanOfficer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clients", uniqueConstraints = @UniqueConstraint(columnNames = {"loan_officer_id", "email"}))
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_officer")
    private LoanOfficer loanOfficer;

    @Column(name = "email", nullable = false, unique = false, length = 150)
    private String email;

    @Column(name = "first_name", nullable = false,length = 120)
    private String firstName;

    @Column(name = "last_name",nullable = false, length = 150)
    private String lastName;

    @Column(name = "phone",nullable = false,length = 15)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

  /*  @Column(name = "ssn_number",nullable = false, unique = true, length = 9)
    private  String ssn;*/

    @Enumerated(EnumType.STRING)
    @Column(name = "state",nullable = false)
    private ServiceStatus serviceStatus;


    public enum ServiceStatus{
        NO_ACTION,LINK_SENT, NEEDS_REVIEW,VERIFIED, LINK_EXPIRED
    }
}
