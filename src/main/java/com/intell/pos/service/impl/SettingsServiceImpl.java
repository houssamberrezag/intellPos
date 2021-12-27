/**
 *
 */
package com.intell.pos.service.impl;

import com.intell.pos.domain.Settings;
import com.intell.pos.repository.SettingsRepository;
import com.intell.pos.service.SettingsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HP
 *
 */
@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private final SettingsRepository settingsRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public Settings save(Settings settings) {
        log.debug("Request to save Settings : {}", settings);
        return settingsRepository.save(settings);
    }

    @Override
    public Optional<Settings> findcurrent() {
        log.debug("Request to get the current Settings");
        return settingsRepository.findFirstByIdIsNotNullOrderByIdDesc();
    }
}
