package com.mockskills.service;

import com.mockskills.model.CollabZone;
import com.mockskills.repository.CollabZoneRepository;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CollabZoneService {

    private static final Logger logger = LoggerFactory.getLogger(CollabZoneService.class);

    private final CollabZoneRepository repository;
    private final JavaMailSender mailSender;

    @Autowired
    public CollabZoneService(CollabZoneRepository repository, JavaMailSender mailSender) {
        this.repository = repository;
        this.mailSender = mailSender;
    }

    // Fetch all registrations
    public List<CollabZone> getAllRegistrations() {
        logger.info("Fetching all registrations.");
        return repository.findAll();
    }

    // Fetch registration by ID
    public ResponseEntity<Object> getRegistrationById(Long id) {
        logger.info("Fetching registration for ID: {}", id);
        CollabZone collabZone = repository.findById(id).orElse(null);

        // If registration is not found, return a user-friendly error message with 404 status
        if (collabZone == null) {
            logger.error("Registration not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Sorry, no registration found with the provided ID. Please verify the ID and try again.");
        }

        // Return the full CollabZone details as JSON with 200 OK status
        logger.info("Registration found for ID: {}. Returning full details.", id);
        return ResponseEntity.ok(collabZone);
    }

    // Create a new registration
    public ResponseEntity<String> createRegistration(CollabZone collabZone) {
        logger.info("Creating new registration for {}", collabZone.getName());

        // Validate inputs
        String validationMessage = validateCollabZone(collabZone);
        if (validationMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationMessage); // Validation failure
        }

        // Check if email already exists
        if (repository.existsByEmail(collabZone.getEmail())) {
            logger.error("Email {} already exists in the system.", collabZone.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("The email you provided is already registered. Please use a different email.");
        }

        // Save the CollabZone entity and generate a formatted ID
        collabZone = saveCollabZoneWithFormattedId(collabZone);

        // Send confirmation email
        sendConfirmationEmail(collabZone);

        // Log success
        logger.info("Registration created successfully for {} with ID: {}", collabZone.getName(), collabZone.getFormattedId());

        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Hello %s, your registration was successful! Your unique ID is: %s. Welcome to MockSkills!", 
                                                                           collabZone.getName(), collabZone.getFormattedId()));
    }

    // Save registration and assign a formatted ID
    private CollabZone saveCollabZoneWithFormattedId(CollabZone collabZone) {
        // Save the CollabZone entity
        CollabZone savedCollabZone = repository.save(collabZone);

        // Generate and update formatted ID
        savedCollabZone.setFormattedId(generateFormattedId(savedCollabZone.getId()));

        // Save again to persist the formatted ID
        return repository.save(savedCollabZone);
    }

    // Generate a formatted ID (e.g., GENZ00001)
    private String generateFormattedId(Long id) {
        return "GENZ" + String.format("%05d", id);
    }

    // Validate CollabZone details
    private String validateCollabZone(CollabZone collabZone) {
        if (collabZone.getName() == null || collabZone.getName().trim().isEmpty()) {
            logger.error("Name is missing for registration.");
            return "Please provide a valid name for registration.";
        }
        if (collabZone.getEmail() == null || collabZone.getEmail().trim().isEmpty()) {
            logger.error("Email is missing for registration.");
            return "Please provide a valid email address.";
        }

        return null; // No validation errors
    }

    private void sendConfirmationEmail(CollabZone collabZone) {
        try {
            logger.info("Sending confirmation email to {}", collabZone.getEmail());

            // Ensure the values are non-null, provide a default if necessary
            String name = collabZone.getName() != null ? collabZone.getName() : "User";
            String registrationId = collabZone.getFormattedId() != null ? collabZone.getFormattedId() : "Unknown";
            String genZId = collabZone.getFormattedId() != null ? collabZone.getFormattedId() : "Unknown";

            // Creating an HTML message with refined styling and tone
            String emailContent = String.format(
                "<html><body>" +
                "<p style='font-size: 14px;'>Dear <b>%s</b>,</p>" +
                "<p style='font-size: 16px; font-weight: bold;'>Welcome to MockSkills!</p>" +
                "<p style='font-size: 14px;'>We are thrilled to have you join our community.</p>" +
                "<p style='font-size: 14px; font-weight: bold;'>Your <span style='font-size: 16px; color: blue;'>Registration ID</span> is: <span style='color: blue;'>%s</span>. Please keep it safe for future reference.</p>" +
                "<p style='font-size: 14px;'>Your profile will be available soon on <span style='font-size: 16px; color: green; font-weight: bold;'>CollabZone</span>. You can search for it using your <span style='font-size: 16px; font-weight: bold;'>GenZ ID: %s</span>. We aim to complete your profile setup within the next 24 hours.</p>" +
                "<p style='font-size: 14px;'>If you have any questions or need support, our <span style='color: red; font-size: 14px; font-weight: bold;'>dedicated support team</span> is here to assist you. Feel free to reach out at any time via <a href='mailto:curiouskundan24@gmail.com'><b>curiouskundan24@gmail.com</b></a>.</p>" +
                "<p style='font-size: 14px;'>We deeply appreciate your participation in MockSkills, and we are committed to providing the support and resources you need to succeed.</p>" +
                "<p style='font-size: 14px;'>Best regards,<br>The MockSkills Team</p>" +
                "</body></html>",
                name, registrationId, genZId
            );

            // Use a MimeMessage for HTML content
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(collabZone.getEmail());
            helper.setSubject("Registration Confirmation - MockSkills");
            helper.setText(emailContent, true); // 'true' indicates HTML content

            mailSender.send(message);
            logger.info("Confirmation email sent successfully to {}", collabZone.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send confirmation email to {}: {}", collabZone.getEmail(), e.getMessage());
        }
    }


}
