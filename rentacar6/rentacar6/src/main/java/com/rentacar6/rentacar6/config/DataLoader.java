package com.rentacar6.rentacar6.config;

import com.rentacar6.rentacar6.model.Admin;
import com.rentacar6.rentacar6.model.Customer;
import com.rentacar6.rentacar6.repository.AdminRepository;
import com.rentacar6.rentacar6.repository.CustomerRepository;
import com.rentacar6.rentacar6.repository.CarRepository;
import com.rentacar6.rentacar6.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Admin Kullanıcı
        Admin admin = new Admin();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@example.com");
        admin.setPhone("123456789");
        admin.setTcNo("12345678912");
        admin.setAddress("123 Admin St");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ROLE_ADMIN"); // Rol atanıyor
        adminRepository.save(admin);

        // Customer Kullanıcı
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("user@example.com");
        customer.setPhone("987654321");
        customer.setTcNo("98765432102");
        customer.setAddress("456 Customer St");
        customer.setPassword(passwordEncoder.encode("user123"));
        customer.setRole("ROLE_USER"); // Rol atanıyor
        customerRepository.save(customer);

        // Araçlar
        Car car1 = new Car("Toyota", "Corolla", 2020, "Red", 50.0, true, "/uploads/cars/Toyota-Corolla-2020-Red.jpg");
        Car car2 = new Car("Honda", "Civic", 2019, "Blue", 45.0, true, "/uploads/cars/Honda-Civic-2019-Blue.jpg");
        Car car3 = new Car("Ford", "Focus", 2021, "Black", 60.0, true, "/uploads/cars/Ford-Focus-2021-Black.jpg");

        carRepository.save(car1);
        carRepository.save(car2);
        carRepository.save(car3);

        System.out.println("Data loaded successfully!");
    }
}
