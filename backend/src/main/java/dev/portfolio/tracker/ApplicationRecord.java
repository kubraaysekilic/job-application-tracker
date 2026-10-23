package dev.portfolio.tracker;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Instant;
@Entity @Table(name="applications")
public class ApplicationRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,length=120) public String company;
 @Column(nullable=false,length=160) public String position;
 @Column(nullable=false,length=1000) public String url="";
 @Column(nullable=false) public LocalDate appliedOn;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) public Status status;
 @Column(nullable=false,length=3000) public String notes="";
 @Column(nullable=false,updatable=false) public Instant createdAt=Instant.now();
 @Version @Column(nullable=false) public Long version;
}
