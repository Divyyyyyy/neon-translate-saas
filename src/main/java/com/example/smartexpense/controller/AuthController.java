package com.example.smartexpense.controller;

import com.example.smartexpense.model.User;
import com.example.smartexpense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    // 1. OTP BHEJNE KA METHOD
    @PostMapping("/send-otp")
    public String sendOtp(
            @RequestParam String email, 
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String profession) {
        
        String otp = String.format("%04d", new Random().nextInt(10000));
        
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setOtp(otp);
        user.setVerified(false);
        
        if (name != null && !name.isEmpty()) user.setName(name);
        if (phone != null && !phone.isEmpty()) user.setPhone(phone);
        if (profession != null && !profession.isEmpty()) user.setProfession(profession);
        
        userRepository.save(user);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("NeonTranslate | Your Neural Access Code");
            message.setText("Oye " + (user.getName() != null ? user.getName() : "User") + ",\n\n" +
                            "Tera secure login OTP ye hai: " + otp + "\n\n" +
                            "Ise kisi ko batana mat!");
            mailSender.send(message);
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    // 2. OTP VERIFY KARNE KA METHOD
    @PostMapping("/verify-otp")
    public Object verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtp() != null && user.getOtp().equals(otp)) {
                user.setVerified(true);
                userRepository.save(user);

                Map<String, String> response = new HashMap<>();
                response.put("status", "VERIFIED");
                response.put("name", user.getName() != null ? user.getName() : "User");
                response.put("email", user.getEmail());
                response.put("phone", user.getPhone() != null ? user.getPhone() : "N/A");
                response.put("profession", user.getProfession() != null ? user.getProfession() : "Neural Explorer");
                response.put("apiKey", user.getApiKey() != null ? user.getApiKey() : "No Key Generated");
                
                return response;
            }
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "INVALID");
        return errorResponse;
    }

    // 3. API KEY GENERATE KARNE KA METHOD (Pro Feature)
    @PostMapping("/generate-api-key")
    public Map<String, String> generateApiKey(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Unique UUID based API Key
            String newKey = "neon_live_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            user.setApiKey(newKey);
            userRepository.save(user);

            response.put("status", "SUCCESS");
            response.put("apiKey", newKey);
        } else {
            response.put("status", "ERROR");
            response.put("message", "User not found");
        }
        return response;
    }

    // 4. BATCH TRANSLATION SIMULATION (SaaS Logic)
    @PostMapping("/batch-translate")
    public Map<String, Object> batchTranslate(@RequestParam String email, @RequestParam int wordCount) {
        Map<String, Object> response = new HashMap<>();
        
        // Simulation: 1 credit per word
        int creditsRequired = wordCount; 
        
        response.put("status", "COMPLETED");
        response.put("processedWords", wordCount);
        response.put("creditsDeducted", creditsRequired);
        response.put("timeTaken", "142ms");
        response.put("engine", "Neural-V3-Turbo");
        
        return response;
    }
}