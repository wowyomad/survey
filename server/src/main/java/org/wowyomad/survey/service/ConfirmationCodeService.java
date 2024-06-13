package org.wowyomad.survey.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionInvocationTargetException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wowyomad.survey.entity.ConfirmationCode;
import org.wowyomad.survey.entity.ConfirmationType;
import org.wowyomad.survey.repository.ConfirmationCodeRepository;
import org.wowyomad.survey.util.CodeGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationCodeService {

    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final EmailService emailService;

    @Autowired
    public ConfirmationCodeService(ConfirmationCodeRepository confirmationCodeRepository, EmailService emailService) {
        this.confirmationCodeRepository = confirmationCodeRepository;
        this.emailService = emailService;
    }


    @Transactional
    public void removeExpiredConfirmationCodes() {
        LocalDateTime now = LocalDateTime.now();
        confirmationCodeRepository.deleteByExpirationTimeBefore(now);
    }

    @Transactional
    public String createConfirmationCode(String email, ConfirmationType type) throws MessagingException {
        String code = CodeGenerator.generateCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);

        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setEmail(email);
        confirmationCode.setCode(code);
        confirmationCode.setExpirationTime(expirationTime);
        confirmationCode.setType(type);

        confirmationCodeRepository.save(confirmationCode);
        emailService.SendEmail(email, "Confirmation Code: ", confirmationCode.getCode());

        return confirmationCode.getCode();
    }

    @Transactional
    public boolean confirmCode(String email, String code, ConfirmationType type) {

        Optional<ConfirmationCode> confirmationCodeOpt = confirmationCodeRepository.findByEmailAndCodeAndType(email, code, type);

        if(confirmationCodeOpt.isPresent()) {
            ConfirmationCode confirmationCode = confirmationCodeOpt.get();
            if(confirmationCode.getExpirationTime().isAfter(LocalDateTime.now()) && !confirmationCode.isConfirmed()) {
                confirmationCode.setConfirmed(true);
                confirmationCodeRepository.save(confirmationCode);
                return true;
            }
        }
        return false;
    }

}
