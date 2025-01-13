package ntu.service_centric.global_dorm.services;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CacheEvictionService {

    private static final Logger logger = Logger.getLogger(CacheEvictionService.class.getName());
    private final CacheManager cacheManager;

    public CacheEvictionService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 3600000) // Clear cache every hour
    public void clearCityCoordinatesCache() {
        Optional.ofNullable(cacheManager.getCache("cityCoordinates"))
                .ifPresent(cache -> {
                    cache.clear();
                    logger.info("Cache 'cityCoordinates' cleared.");
                });
    }
}
