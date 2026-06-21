package NguyenMinhThien.authapp.config;

import NguyenMinhThien.authapp.entity.User;
import NguyenMinhThien.authapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * On startup, copies the bundled default avatar (packaged at
 * resources/avatars-seed/default-avatar.jpg) into the runtime uploads
 * directory and assigns it to any existing user that doesn't already
 * have an avatarUrl set. Runs after DataInitializer (which has Order(1)).
 */
@Component
@RequiredArgsConstructor
@Slf4j
@org.springframework.core.annotation.Order(2)
@Profile("!test")
public class AvatarSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final String SEED_RESOURCE = "avatars-seed/default-avatar.jpg";
    private static final String SEED_FILENAME = "default-avatar.jpg";

    @Override
    public void run(String... args) throws Exception {
        Path avatarDir = Paths.get(uploadDir, "avatars");
        Files.createDirectories(avatarDir);
        Path destination = avatarDir.resolve(SEED_FILENAME);

        if (!Files.exists(destination)) {
            try (InputStream in = new ClassPathResource(SEED_RESOURCE).getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
                log.info(">>> Seeded default avatar to {}", destination);
            } catch (IOException e) {
                log.warn(">>> Could not seed default avatar: {}", e.getMessage());
                return;
            }
        }

        String publicUrl = "/uploads/avatars/" + SEED_FILENAME;
        List<User> allUsers = userRepository.findAll();

        if (!allUsers.isEmpty()) {
            allUsers.forEach(u -> u.setAvatarUrl(publicUrl));
            userRepository.saveAll(allUsers);
            log.info(">>> Forced default avatar for {} user(s) (including ones with an existing avatarUrl)", allUsers.size());
        }
    }
}
