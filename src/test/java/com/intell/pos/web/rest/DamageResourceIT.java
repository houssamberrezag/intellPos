package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.Damage;
import com.intell.pos.repository.DamageRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DamageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamageResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/damages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DamageRepository damageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamageMockMvc;

    private Damage damage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Damage createEntity(EntityManager em) {
        Damage damage = new Damage()
            .quantity(DEFAULT_QUANTITY)
            .date(DEFAULT_DATE)
            .note(DEFAULT_NOTE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return damage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Damage createUpdatedEntity(EntityManager em) {
        Damage damage = new Damage()
            .quantity(UPDATED_QUANTITY)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return damage;
    }

    @BeforeEach
    public void initTest() {
        damage = createEntity(em);
    }

    @Test
    @Transactional
    void createDamage() throws Exception {
        int databaseSizeBeforeCreate = damageRepository.findAll().size();
        // Create the Damage
        restDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(damage)))
            .andExpect(status().isCreated());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeCreate + 1);
        Damage testDamage = damageList.get(damageList.size() - 1);
        assertThat(testDamage.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testDamage.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDamage.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testDamage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDamage.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createDamageWithExistingId() throws Exception {
        // Create the Damage with an existing ID
        damage.setId(1L);

        int databaseSizeBeforeCreate = damageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(damage)))
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = damageRepository.findAll().size();
        // set the field null
        damage.setQuantity(null);

        // Create the Damage, which fails.

        restDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(damage)))
            .andExpect(status().isBadRequest());

        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDamages() throws Exception {
        // Initialize the database
        damageRepository.saveAndFlush(damage);

        // Get all the damageList
        restDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damage.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getDamage() throws Exception {
        // Initialize the database
        damageRepository.saveAndFlush(damage);

        // Get the damage
        restDamageMockMvc
            .perform(get(ENTITY_API_URL_ID, damage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damage.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDamage() throws Exception {
        // Get the damage
        restDamageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDamage() throws Exception {
        // Initialize the database
        damageRepository.saveAndFlush(damage);

        int databaseSizeBeforeUpdate = damageRepository.findAll().size();

        // Update the damage
        Damage updatedDamage = damageRepository.findById(damage.getId()).get();
        // Disconnect from session so that the updates on updatedDamage are not directly saved in db
        em.detach(updatedDamage);
        updatedDamage
            .quantity(UPDATED_QUANTITY)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDamage))
            )
            .andExpect(status().isOk());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
        Damage testDamage = damageList.get(damageList.size() - 1);
        assertThat(testDamage.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDamage.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDamage.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testDamage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDamage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingDamage() throws Exception {
        int databaseSizeBeforeUpdate = damageRepository.findAll().size();
        damage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamage() throws Exception {
        int databaseSizeBeforeUpdate = damageRepository.findAll().size();
        damage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamage() throws Exception {
        int databaseSizeBeforeUpdate = damageRepository.findAll().size();
        damage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(damage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamageWithPatch() throws Exception {
        // Initialize the database
        damageRepository.saveAndFlush(damage);

        int databaseSizeBeforeUpdate = damageRepository.findAll().size();

        // Update the damage using partial update
        Damage partialUpdatedDamage = new Damage();
        partialUpdatedDamage.setId(damage.getId());

        partialUpdatedDamage.quantity(UPDATED_QUANTITY).note(UPDATED_NOTE).updatedAt(UPDATED_UPDATED_AT);

        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDamage))
            )
            .andExpect(status().isOk());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
        Damage testDamage = damageList.get(damageList.size() - 1);
        assertThat(testDamage.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDamage.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDamage.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testDamage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDamage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateDamageWithPatch() throws Exception {
        // Initialize the database
        damageRepository.saveAndFlush(damage);

        int databaseSizeBeforeUpdate = damageRepository.findAll().size();

        // Update the damage using partial update
        Damage partialUpdatedDamage = new Damage();
        partialUpdatedDamage.setId(damage.getId());

        partialUpdatedDamage
            .quantity(UPDATED_QUANTITY)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDamage))
            )
            .andExpect(status().isOk());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
        Damage testDamage = damageList.get(damageList.size() - 1);
        assertThat(testDamage.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDamage.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDamage.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testDamage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDamage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingDamage() throws Exception {
        int databaseSizeBeforeUpdate = damageRepository.findAll().size();
        damage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamage() throws Exception {
        int databaseSizeBeforeUpdate = damageRepository.findAll().size();
        damage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamage() throws Exception {
        int databaseSizeBeforeUpdate = damageRepository.findAll().size();
        damage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(damage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Damage in the database
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamage() throws Exception {
        // Initialize the database
        damageRepository.saveAndFlush(damage);

        int databaseSizeBeforeDelete = damageRepository.findAll().size();

        // Delete the damage
        restDamageMockMvc
            .perform(delete(ENTITY_API_URL_ID, damage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Damage> damageList = damageRepository.findAll();
        assertThat(damageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
