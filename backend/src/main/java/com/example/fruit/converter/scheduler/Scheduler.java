package com.example.fruit.converter.scheduler;

import com.example.fruit.converter.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically checks whether exchange rates are stale and triggers a CBR refresh.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ExchangeRateService exchangeRateService;

    /**
     * Runs every hour. Skips the fetch when fresh rates already exist.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void scheduleTask() {
        try {
            if (!exchangeRateService.existsExpiredOrMissing()) {
                log.info("Exchange rates fresh. Skipping scheduled fetch.");
                return;
            }
            log.warn("Expired or missing rates present. Fetching...");
            exchangeRateService.fetchAndStoreRates();
        } catch (Exception e) {
            log.error("Error during scheduled exchange rates fetch", e);
        }
    }
}
