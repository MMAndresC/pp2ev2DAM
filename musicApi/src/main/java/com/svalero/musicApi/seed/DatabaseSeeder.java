package com.svalero.musicApi.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DatabaseSeeder {
    private final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner seedDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            String fields = "";
            String values = "";
            // Only when tables are empty
            //USER
            int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
            if (count == 0) {
                this.logger.info("Seeding user table...");
                String encodedPassword = passwordEncoder.encode("1234");
                fields = "INSERT INTO user (email, password, role) VALUES ";
                values = "('demo@example.com', '" + encodedPassword + "', 'user')";
                jdbcTemplate.execute(fields  + values);
                this.logger.info("Seeding completed");
            }else{
                this.logger.info("user table already seeded. Skipping...");
            }

            //ARTIST
            count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM artist", Integer.class);
            if (count == 0) {
                this.logger.info("Seeding artist table...");
                fields = "INSERT INTO artist (name, registration_date, is_soloist, country) VALUES ";
                values = "('Roxette', '2005-02-15', false, 'Sweden')";
                jdbcTemplate.execute(fields  + values);
                values = "('Beast in black', '2015-04-28', false, 'Finland')";
                jdbcTemplate.execute(fields + values);

                values = "('Olly Alexander', '2021-07-03', true, 'UK')";
                jdbcTemplate.execute(fields + values);

                this.logger.info("Seeding completed");
            } else {
                this.logger.info("artist table already seeded. Skipping...");
            }

            //ALBUM
            count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM album", Integer.class);
            if (count == 0) {
                this.logger.info("Seeding album table...");
                fields = "INSERT INTO album (title,  artist_id, release_date, genre,  duration,  tracks, label) VALUES ";
                values = "('Joyride', 1, '1985-03-25', 'pop', 59.2, 15, 'EMI')";
                jdbcTemplate.execute(fields  + values);
                values = "('Tourism', 1, '1992-08-28', 'pop', 70.37, 16, 'EMI')";
                jdbcTemplate.execute(fields + values);

                values = "('From hell with love', 2, '2019-02-08', 'metal', 43.08, 13, 'Nuclear Blast')";
                jdbcTemplate.execute(fields + values);

                values = "('Polar', 3, '2025-02-07', 'electropop', 40.15, 13, 'Polydor')";
                jdbcTemplate.execute(fields + values);

                this.logger.info("Seeding completed");
            } else {
                this.logger.info("album table already seeded. Skipping...");
            }
        };
    }
}
