package com.frota_manager.inteligent_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um condutor na frota
 * Utiliza anotações JPA para mapeamento objeto-relacional
 * Inclui validações para garantir integridade dos dados
 */
@Entity
@Table(name = "drivers")
public class Driver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "O número de identificação é obrigatório")
    @Pattern(regexp = "^[0-9]{8}$", message = "O número de identificação deve ter 8 dígitos")
    @Column(name = "identification_number", nullable = false, unique = true)
    private String identificationNumber;
    
    @NotBlank(message = "O número da carta de condução é obrigatório")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{9}$", message = "A carta de condução deve seguir o formato XX123456789")
    @Column(name = "driver_license_number", nullable = false, unique = true)
    private String driverLicenseNumber;
    
    @NotNull(message = "A data de validade da carta é obrigatória")
    @Column(name = "license_expiry_date", nullable = false)
    private LocalDate licenseExpiryDate;
    
    @Email(message = "O email deve ser válido")
    @Column(name = "email")
    private String email;
    
    @Pattern(regexp = "^[0-9]{9}$", message = "O telefone deve ter 9 dígitos")
    @Column(name = "phone")
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverStatus status = DriverStatus.ACTIVE;
    
    @Column(name = "total_trips")
    private Integer totalTrips = 0;
    
    @Column(name = "total_kilometers")
    private Double totalKilometers = 0.0;
    
    @Column(name = "rating")
    private Double rating = 5.0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Relacionamentos
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trip> trips = new ArrayList<>();
    
    // Construtores
    public Driver() {}
    
    public Driver(String name, String identificationNumber, String driverLicenseNumber, LocalDate licenseExpiryDate) {
        this.name = name;
        this.identificationNumber = identificationNumber;
        this.driverLicenseNumber = driverLicenseNumber;
        this.licenseExpiryDate = licenseExpiryDate;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
    
    public String getDriverLicenseNumber() { return driverLicenseNumber; }
    public void setDriverLicenseNumber(String driverLicenseNumber) { this.driverLicenseNumber = driverLicenseNumber; }
    
    public LocalDate getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }
    
    public Integer getTotalTrips() { return totalTrips; }
    public void setTotalTrips(Integer totalTrips) { this.totalTrips = totalTrips; }
    
    public Double getTotalKilometers() { return totalKilometers; }
    public void setTotalKilometers(Double totalKilometers) { this.totalKilometers = totalKilometers; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Trip> getTrips() { return trips; }
    public void setTrips(List<Trip> trips) { this.trips = trips; }
    
    // Métodos de negócio
    public boolean isActive() {
        return status == DriverStatus.ACTIVE;
    }
    
    public boolean isLicenseValid() {
        return licenseExpiryDate != null && licenseExpiryDate.isAfter(LocalDate.now());
    }
    
    public boolean isLicenseExpiringSoon() {
        return licenseExpiryDate != null && 
               licenseExpiryDate.isAfter(LocalDate.now()) && 
               licenseExpiryDate.isBefore(LocalDate.now().plusMonths(3));
    }
    
    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", status=" + status +
                '}';
    }
} 