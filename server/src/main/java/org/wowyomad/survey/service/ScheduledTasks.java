package org.wowyomad.survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {

    private static final float rate = 0.5f;

    private final ConfirmationCodeService confirmationCodeService;
    private final UserService userService;

    @Autowired
    public ScheduledTasks(
            ConfirmationCodeService confirmationCodeService,
            UserService userService
    ) {
        this.confirmationCodeService = confirmationCodeService;
        this.userService = userService;
    }

    @Scheduled(fixedRate = (long)(60000 * rate))
    public void removeExpiredConfirmationCode() {
        confirmationCodeService.removeExpiredConfirmationCodes();
    }

    @Scheduled(fixedRate = (long)(60000 * rate))
    public void removeUnconfirmedUsers() {
        userService.removeExpiredUnconfirmedUsers();
    }


}
