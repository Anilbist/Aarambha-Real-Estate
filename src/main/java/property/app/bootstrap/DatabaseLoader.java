package property.app.bootstrap;

import property.app.repository.AddRepository;
import property.app.repository.UserRepository;
import property.app.beans.Role;
import property.app.beans.User;
import property.app.repository.CommentRepository;
import property.app.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private AddRepository addRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    private Map<String,User> users = new HashMap<>();


    public DatabaseLoader(AddRepository addRepository, CommentRepository commentRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.addRepository = addRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
    try {
        if(roleRepository.findByName("ROLE_USER")!=null){
            return;
        }
        // add users and roles
        addUserAndRoles();

    }catch (Exception e){
        System.out.println(e.getMessage());
        System.out.println("DB already initialized");
    }
    }


    private void addUserAndRoles() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = "{bcrypt}" + encoder.encode("password");

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);

        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setFullName("Admin Name");
        admin.setPhone("123456789");
        admin.addRole(adminRole);
        admin.setPassword(secret);
        admin.setConfirmPassword(secret);
        admin.setUsername("admin");
        admin.setEnabled(true);
        userRepository.save(admin);

    }
}
