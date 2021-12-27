/**
 *
 */
package com.intell.pos.web.rest;

import com.intell.pos.domain.Settings;
import com.intell.pos.repository.SettingsRepository;
import com.intell.pos.service.SettingsService;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * @author HP
 *
 */
@RestController
@RequestMapping("/api")
public class SettingsResource {

    private final Logger log = LoggerFactory.getLogger(SettingsResource.class);

    private static final String ENTITY_NAME = "settings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettingsService settingsService;

    private final SettingsRepository settingsRepository;

    public SettingsResource(SettingsService settingsService, SettingsRepository settingsRepository) {
        super();
        this.settingsService = settingsService;
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("/settings/current")
    public ResponseEntity<Settings> getCurrentSettings() {
        log.debug("REST request to get the current settings");
        Optional<Settings> settings = settingsService.findcurrent();
        return ResponseEntity.ok(settings.isPresent() ? settings.get() : new Settings());
    }

    /**
     * {@code PUT  /settingss/:id} : Updates an existing settings.
     *
     * @param id the id of the settings to save.
     * @param settings the settings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settings,
     * or with status {@code 400 (Bad Request)} if the settings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settings couldn't be updated.
     * @throws Exception
     */
    @PutMapping("/settings/{id}")
    public ResponseEntity<Settings> updateSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Settings settings
    ) throws Exception {
        log.debug("REST request to update Settings : {}, {}", id, settings);
        if (settings.getId() == null) {
            throw new Exception("Invalid id");
        }
        if (!Objects.equals(id, settings.getId())) {
            throw new Exception("Invalid ID");
        }

        if (!settingsRepository.existsById(id)) {
            throw new Exception("Entity not found");
        }

        Settings result = settingsService.save(settings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settings.getId().toString()))
            .body(result);
    }

    @PostMapping("/settings")
    public ResponseEntity<Settings> createSettings(@Valid @RequestBody Settings settings) throws Exception {
        log.debug("REST request to save Settings : {}", settings);
        if (settings.getId() != null) {
            throw new Exception("A new settings cannot already have an ID");
        }
        Settings result = settingsService.save(settings);
        return ResponseEntity
            .created(new URI("/api/settingss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
