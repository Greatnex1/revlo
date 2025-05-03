package com.nouah.revlo.ai;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InventoryAlertScheduler {
    private final InventoryAlertService alertService;



    @Scheduled(cron = "0 0 8 * * ?") // Every day at 8 AM
    public void runDailyInventoryCheck() {
        alertService.checkInventoryLevels();
    }

}
