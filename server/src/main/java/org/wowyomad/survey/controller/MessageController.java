package org.wowyomad.survey.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wowyomad.survey.entity.ConfirmationCode;
import org.wowyomad.survey.entity.ConfirmationType;
import org.wowyomad.survey.repository.ConfirmationCodeRepository;
import org.wowyomad.survey.service.ConfirmationCodeService;
import org.wowyomad.survey.service.EmailService;
import org.wowyomad.survey.util.CodeGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    EmailService emailService;
    private ConfirmationCodeService confirmationCodeService;

    @Autowired
    public MessageController(EmailService emailService, ConfirmationCodeService confirmationCodeService) {
        this.emailService = emailService;
        this.confirmationCodeService = confirmationCodeService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCode(
            @RequestParam("email") String email,
            @RequestParam("type") ConfirmationType type
    ) throws MessagingException {
        confirmationCodeService.createConfirmationCode(email, type);
        return ResponseEntity.ok("Confirmation Code sent to " + email);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code,
            @RequestParam("type") ConfirmationType type
    ) {
        boolean isConfirmed = confirmationCodeService.confirmCode(email, code, type);
        if (isConfirmed) {
            String responseBody = """
                    <html>
                    <body>
                        <p>Code confirmed successfully. Redirecting to the test page...</p>
                        <script>
                            setTimeout(function() {
                                window.location.href = '/test';
                                }, 3000);
                        </script>
                    </body>
                    </html>
                    """;
            return ResponseEntity.ok().header("Content-Type", "text/html").body(responseBody);
        } else {
            return ResponseEntity.status(400).body("Invalid or expired confirmation code.");
        }
    }
}
