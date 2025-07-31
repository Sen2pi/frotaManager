package com.frota_manager.inteligent_manager.config;

import com.frota_manager.inteligent_manager.model.*;
import com.frota_manager.inteligent_manager.repository.DriverRepository;
import com.frota_manager.inteligent_manager.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Loader para popular a base de dados com dados de exemplo
 * Utiliza CommandLineRunner para executar na inicialização da aplicação
 */
@Component
public class DataLoader implements CommandLineRunner {
    
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    
    @Autowired
    public DataLoader(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        loadVehicles();
        loadDrivers();
    }
    
    /**
     * Carrega veículos de exemplo
     */
    private void loadVehicles() {
        if (vehicleRepository.count() == 0) {
            // Veículos Toyota
            Vehicle toyotaCorolla = new Vehicle("Toyota", "Corolla", "AB-12-CD", 2020);
            toyotaCorolla.setFuelType(FuelType.GASOLINE);
            toyotaCorolla.setFuelCapacity(50.0);
            toyotaCorolla.setCurrentFuelLevel(35.0);
            toyotaCorolla.setTotalKilometers(15000.0);
            toyotaCorolla.setStatus(VehicleStatus.AVAILABLE);
            toyotaCorolla.setNextMaintenanceDate(LocalDateTime.now().plusMonths(2));
            vehicleRepository.save(toyotaCorolla);
            
            Vehicle toyotaCamry = new Vehicle("Toyota", "Camry", "EF-34-GH", 2021);
            toyotaCamry.setFuelType(FuelType.HYBRID);
            toyotaCamry.setFuelCapacity(55.0);
            toyotaCamry.setCurrentFuelLevel(45.0);
            toyotaCamry.setTotalKilometers(8000.0);
            toyotaCamry.setStatus(VehicleStatus.IN_USE);
            toyotaCamry.setNextMaintenanceDate(LocalDateTime.now().plusMonths(4));
            vehicleRepository.save(toyotaCamry);
            
            // Veículos Honda
            Vehicle hondaCivic = new Vehicle("Honda", "Civic", "IJ-56-KL", 2019);
            hondaCivic.setFuelType(FuelType.GASOLINE);
            hondaCivic.setFuelCapacity(47.0);
            hondaCivic.setCurrentFuelLevel(10.0); // Baixo nível
            hondaCivic.setTotalKilometers(25000.0);
            hondaCivic.setStatus(VehicleStatus.AVAILABLE);
            hondaCivic.setNextMaintenanceDate(LocalDateTime.now().minusDays(5)); // Precisa manutenção
            vehicleRepository.save(hondaCivic);
            
            Vehicle hondaAccord = new Vehicle("Honda", "Accord", "MN-78-OP", 2022);
            hondaAccord.setFuelType(FuelType.ELECTRIC);
            hondaAccord.setFuelCapacity(75.0);
            hondaAccord.setCurrentFuelLevel(60.0);
            hondaAccord.setTotalKilometers(5000.0);
            hondaAccord.setStatus(VehicleStatus.AVAILABLE);
            hondaAccord.setNextMaintenanceDate(LocalDateTime.now().plusMonths(6));
            vehicleRepository.save(hondaAccord);
            
            // Veículos BMW
            Vehicle bmwX3 = new Vehicle("BMW", "X3", "QR-90-ST", 2021);
            bmwX3.setFuelType(FuelType.DIESEL);
            bmwX3.setFuelCapacity(65.0);
            bmwX3.setCurrentFuelLevel(25.0);
            bmwX3.setTotalKilometers(18000.0);
            bmwX3.setStatus(VehicleStatus.MAINTENANCE);
            bmwX3.setNextMaintenanceDate(LocalDateTime.now().minusDays(10));
            vehicleRepository.save(bmwX3);
            
            Vehicle bmwX5 = new Vehicle("BMW", "X5", "UV-12-WX", 2020);
            bmwX5.setFuelType(FuelType.DIESEL);
            bmwX5.setFuelCapacity(80.0);
            bmwX5.setCurrentFuelLevel(15.0); // Baixo nível
            bmwX5.setTotalKilometers(30000.0);
            bmwX5.setStatus(VehicleStatus.OUT_OF_SERVICE);
            bmwX5.setNextMaintenanceDate(LocalDateTime.now().minusDays(15));
            vehicleRepository.save(bmwX5);
            
            // Veículos Mercedes
            Vehicle mercedesC = new Vehicle("Mercedes", "Classe C", "YZ-34-AB", 2022);
            mercedesC.setFuelType(FuelType.GASOLINE);
            mercedesC.setFuelCapacity(66.0);
            mercedesC.setCurrentFuelLevel(50.0);
            mercedesC.setTotalKilometers(12000.0);
            mercedesC.setStatus(VehicleStatus.AVAILABLE);
            mercedesC.setNextMaintenanceDate(LocalDateTime.now().plusMonths(3));
            vehicleRepository.save(mercedesC);
            
            Vehicle mercedesE = new Vehicle("Mercedes", "Classe E", "CD-56-EF", 2021);
            mercedesE.setFuelType(FuelType.HYBRID);
            mercedesE.setFuelCapacity(70.0);
            mercedesE.setCurrentFuelLevel(40.0);
            mercedesE.setTotalKilometers(22000.0);
            mercedesE.setStatus(VehicleStatus.AVAILABLE);
            mercedesE.setNextMaintenanceDate(LocalDateTime.now().plusMonths(1));
            vehicleRepository.save(mercedesE);
            
            System.out.println("✅ Veículos de exemplo carregados com sucesso!");
        }
    }
    
    /**
     * Carrega condutores de exemplo
     */
    private void loadDrivers() {
        if (driverRepository.count() == 0) {
            // Condutores ativos
            Driver joaoSilva = new Driver("João Silva", "12345678", "PT123456789", LocalDate.now().plusYears(2));
            joaoSilva.setEmail("joao.silva@alten.com");
            joaoSilva.setPhone("912345678");
            joaoSilva.setStatus(DriverStatus.ACTIVE);
            joaoSilva.setTotalTrips(45);
            joaoSilva.setTotalKilometers(8500.0);
            joaoSilva.setRating(4.8);
            driverRepository.save(joaoSilva);
            
            Driver mariaSantos = new Driver("Maria Santos", "23456789", "PT234567890", LocalDate.now().plusYears(1));
            mariaSantos.setEmail("maria.santos@alten.com");
            mariaSantos.setPhone("923456789");
            mariaSantos.setStatus(DriverStatus.ACTIVE);
            mariaSantos.setTotalTrips(32);
            mariaSantos.setTotalKilometers(6200.0);
            mariaSantos.setRating(4.9);
            driverRepository.save(mariaSantos);
            
            Driver pedroOliveira = new Driver("Pedro Oliveira", "34567890", "PT345678901", LocalDate.now().plusMonths(6));
            pedroOliveira.setEmail("pedro.oliveira@alten.com");
            pedroOliveira.setPhone("934567890");
            pedroOliveira.setStatus(DriverStatus.ON_TRIP);
            pedroOliveira.setTotalTrips(28);
            pedroOliveira.setTotalKilometers(4800.0);
            pedroOliveira.setRating(4.5);
            driverRepository.save(pedroOliveira);
            
            Driver anaCosta = new Driver("Ana Costa", "45678901", "PT456789012", LocalDate.now().plusMonths(3));
            anaCosta.setEmail("ana.costa@alten.com");
            anaCosta.setPhone("945678901");
            anaCosta.setStatus(DriverStatus.ACTIVE);
            anaCosta.setTotalTrips(15);
            anaCosta.setTotalKilometers(2800.0);
            anaCosta.setRating(4.2);
            driverRepository.save(anaCosta);
            
            // Condutores com carta a expirar
            Driver carlosRibeiro = new Driver("Carlos Ribeiro", "56789012", "PT567890123", LocalDate.now().plusMonths(2));
            carlosRibeiro.setEmail("carlos.ribeiro@alten.com");
            carlosRibeiro.setPhone("956789012");
            carlosRibeiro.setStatus(DriverStatus.ACTIVE);
            carlosRibeiro.setTotalTrips(38);
            carlosRibeiro.setTotalKilometers(7200.0);
            carlosRibeiro.setRating(4.7);
            driverRepository.save(carlosRibeiro);
            
            // Condutor com carta expirada
            Driver luciaFerreira = new Driver("Lúcia Ferreira", "67890123", "PT678901234", LocalDate.now().minusDays(30));
            luciaFerreira.setEmail("lucia.ferreira@alten.com");
            luciaFerreira.setPhone("967890123");
            luciaFerreira.setStatus(DriverStatus.SUSPENDED);
            luciaFerreira.setTotalTrips(22);
            luciaFerreira.setTotalKilometers(4100.0);
            luciaFerreira.setRating(4.1);
            driverRepository.save(luciaFerreira);
            
            // Condutor inativo
            Driver manuelAlves = new Driver("Manuel Alves", "78901234", "PT789012345", LocalDate.now().plusYears(3));
            manuelAlves.setEmail("manuel.alves@alten.com");
            manuelAlves.setPhone("978901234");
            manuelAlves.setStatus(DriverStatus.INACTIVE);
            manuelAlves.setTotalTrips(55);
            manuelAlves.setTotalKilometers(10500.0);
            manuelAlves.setRating(4.6);
            driverRepository.save(manuelAlves);
            
            // Condutor de férias
            Driver sofiaMartins = new Driver("Sofia Martins", "89012345", "PT890123456", LocalDate.now().plusYears(1));
            sofiaMartins.setEmail("sofia.martins@alten.com");
            sofiaMartins.setPhone("989012345");
            sofiaMartins.setStatus(DriverStatus.ON_LEAVE);
            sofiaMartins.setTotalTrips(19);
            sofiaMartins.setTotalKilometers(3600.0);
            sofiaMartins.setRating(4.3);
            driverRepository.save(sofiaMartins);
            
            System.out.println("✅ Condutores de exemplo carregados com sucesso!");
        }
    }
} 