package com.svalero.musicApi.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
//Not use in production
@Profile("dev")
public class DatabaseSeeder {
    private final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    @Bean
    public CommandLineRunner seedDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            String fields = "";
            String values = "";
            // Only when tables are empty
            //ARTIST
            int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM artist", Integer.class);
            if (count == 0) {
                this.logger.info("Seeding artist table...");
                fields = "INSERT INTO artist (name, registration_date, is_soloist, country, image) VALUES ";
                values = "('Roxette', '2005-02-15', false, 'Sweden', 'artist1.png')";
                jdbcTemplate.execute(fields  + values);
                values = "('Beast in black', '2015-04-28', false, 'Finland', 'artist2.png')";
                jdbcTemplate.execute(fields + values);

                values = "('Olly Alexander', '2021-07-03', true, 'UK', 'artist3.png')";
                jdbcTemplate.execute(fields + values);

                this.logger.info("Seeding completed");
            } else {
                this.logger.info("artist table already seeded. Skipping...");
            }

            //ALBUM
            count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM album", Integer.class);
            if (count == 0) {
                this.logger.info("Seeding album table...");
                fields = "INSERT INTO album (title,  artist_id, release_date, genre,  duration,  tracks, label, front) VALUES ";
                values = "('Joyride', 1, '1985-03-25', 'pop', 59.2, 15, 'EMI', 'album1.png')";
                jdbcTemplate.execute(fields  + values);
                values = "('Tourism', 1, '1992-08-28', 'pop', 70.37, 16, 'EMI', 'album2.png')";
                jdbcTemplate.execute(fields + values);

                values = "('From hell with love', 2, '2019-02-08', 'metal', 43.08, 13, 'Nuclear Blast', 'album3.png')";
                jdbcTemplate.execute(fields + values);

                values = "('Polar', 3, '2025-02-07', 'electropop', 40.15, 13, 'Polydor', 'album4.png')";
                jdbcTemplate.execute(fields + values);

                this.logger.info("Seeding completed");
            } else {
                this.logger.info("album table already seeded. Skipping...");
            }
        };
    }
}
