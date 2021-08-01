package property.app.service;

import property.app.beans.Adds;
import property.app.beans.Message;
import property.app.beans.User;
import property.app.repository.AddRepository;
import property.app.repository.MessageRepository;
import property.app.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AddRepository addRepository;
    private final MessageRepository messageRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService, MessageRepository messageRepository, AddRepository addRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.messageRepository = messageRepository;
        this.addRepository = addRepository;
        encoder = new BCryptPasswordEncoder();
    }

    public User register(User user) {
        // Take the password from the form and encode
        String secret = "{bcrypt}"+encoder.encode(user.getPassword());
        user.setPassword(secret);
        user.setConfirmPassword(secret);
        user.setEnabled(true);
        // Assigning a role to the user
        user.addRole(roleService.findByName("ROLE_USER"));
        // Save the user.
        save(user);
        // Return user.
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    @Transactional
    public void saveUsers(User... users) {
        for(User user : users) {
            logger.info("Saving User: " + user.getEmail());
            userRepository.save(user);
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessagesByRecieverId(Long id) {
        return messageRepository.findByReceiverId(id);
    }

    public List<Message> getMessagesBySenderId(Long id) {
        return messageRepository.findBySenderId(id);
    }

    public void deleteById(Long id) {
        List<Message> messagesRec = messageRepository.findByReceiverId(id);
        if(messagesRec!=null){
            for (Message message : messagesRec){
                messageRepository.deleteById(message.getId());
            }
        }
        List<Message> messagesSen = messageRepository.findBySenderId(id);
        if(messagesSen!=null){
            for (Message message : messagesSen){
                messageRepository.deleteById(message.getId());
            }
        }
        List<Adds> adds = addRepository.findByUserId(id);
        if(adds!=null){
            for (Adds add : adds){
                addRepository.deleteById(add.getId());
            }
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<User> nonAdminUsers = new ArrayList<>();
        if(null!=users){
            for(User user : users){
                if(!user.getEmail().equals("admin@admin.com")){
                    nonAdminUsers.add(user);
                }
            }
        }
        return nonAdminUsers;
    }
}
