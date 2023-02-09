package com.bitcoin.interview.config;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.repository.PaymentRepository;
import com.bitcoin.interview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PaymentRepository paymentRepository) {
        return args -> {
            //Database for demo purpose
            User user01 = new User("test01", "test01@email.com", "abc");
            User user02 = new User("test02", "test02@email.com", "abc");
            User user03 = new User("test03", "test03@email.com", "abc", true);

            logger.info("Insert data: {}", userRepository.save(user01));
            logger.info("Insert data: {}" , userRepository.save(user02));
            logger.info("Insert data: {}", userRepository.save(user03));

            //User01 and user02 has payments, while user03 has no payments
            logger.info("Insert data: {}",
                    paymentRepository.save(new Payment(2400.0, 240.0, "abc", user01)));
            logger.info("Insert data: {}",
                    paymentRepository.save(new Payment(2300.0, 230.0, "abc", user01)));
            logger.info("Insert data: {}",
                    paymentRepository.save(new Payment(2200.0, 220.0, "abc", user02)));
            logger.info("Insert data: {}",
                    paymentRepository.save(new Payment(2100.0, 210.0, "abc", user02)));

        };
    }
}
