package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.User;
import bsuir.coursework.HairSalon.models.ResetCode;
import bsuir.coursework.HairSalon.repositories.UserRepository;
import bsuir.coursework.HairSalon.repositories.ResetCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static bsuir.coursework.HairSalon.utils.PasswordHasher.hashPassword;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ResetCodeRepository resetCodeRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, ResetCodeRepository resetCodeRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.resetCodeRepository = resetCodeRepository;
        this.emailService = emailService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(int id, User updatedUser) throws NoSuchAlgorithmException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(hashPassword(updatedUser.getPassword()));
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String generateAndSendResetCode(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String resetCode = generateCode();
            saveResetCode(email, resetCode);
            emailService.sendResetCodeByEmail(email, resetCode);
            return resetCode;
        } else {
            return null;
        }
    }

    public boolean checkResetCode(String email, String resetCode) {
        ResetCode storedResetCode = resetCodeRepository.findByUserEmailAndCode(email, resetCode);
        if (storedResetCode != null) {
            LocalDateTime creationTime = storedResetCode.getCreationTime();
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(creationTime, currentTime);
            long minutesPassed = duration.toMinutes();
            return minutesPassed <= 5;
        }
        return false;
    }

    public boolean resetPassword(String email, String resetCode, String newPassword) throws NoSuchAlgorithmException {
        if (checkResetCode(email, resetCode)) {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setPassword(hashPassword(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    private String generateCode() {
        int codeLength = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            char randomChar = characters.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }

    private void saveResetCode(String email, String resetCode) {
        ResetCode codeEntity = new ResetCode();
        codeEntity.setCode(resetCode);
        codeEntity.setUser(userRepository.findByEmail(email));
        codeEntity.setCreationTime(LocalDateTime.now());
        resetCodeRepository.save(codeEntity);
    }

}
