package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.Taxe;
import com.intell.pos.repository.TaxeRepository;
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
 * Integration tests for the {@link TaxeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/taxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxeRepository taxeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxeMockMvc;

    private Taxe taxe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taxe createEntity(EntityManager em) {
        Taxe taxe = new Taxe()
            .name(DEFAULT_NAME)
            .rate(DEFAULT_RATE)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return taxe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taxe createUpdatedEntity(EntityManager em) {
        Taxe taxe = new Taxe()
            .name(UPDATED_NAME)
            .rate(UPDATED_RATE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return taxe;
    }

    @BeforeEach
    public void initTest() {
        taxe = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxe() throws Exception {
        int databaseSizeBeforeCreate = taxeRepository.findAll().size();
        // Create the Taxe
        restTaxeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isCreated());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeCreate + 1);
        Taxe testTaxe = taxeList.get(taxeList.size() - 1);
        assertThat(testTaxe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxe.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testTaxe.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTaxe.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTaxe.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTaxeWithExistingId() throws Exception {
        // Create the Taxe with an existing ID
        taxe.setId(1L);

        int databaseSizeBeforeCreate = taxeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isBadRequest());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxeRepository.findAll().size();
        // set the field null
        taxe.setName(null);

        // Create the Taxe, which fails.

        restTaxeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isBadRequest());

        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxeRepository.findAll().size();
        // set the field null
        taxe.setRate(null);

        // Create the Taxe, which fails.

        restTaxeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isBadRequest());

        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxeRepository.findAll().size();
        // set the field null
        taxe.setType(null);

        // Create the Taxe, which fails.

        restTaxeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isBadRequest());

        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxes() throws Exception {
        // Initialize the database
        taxeRepository.saveAndFlush(taxe);

        // Get all the taxeList
        restTaxeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxe.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTaxe() throws Exception {
        // Initialize the database
        taxeRepository.saveAndFlush(taxe);

        // Get the taxe
        restTaxeMockMvc
            .perform(get(ENTITY_API_URL_ID, taxe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxe.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTaxe() throws Exception {
        // Get the taxe
        restTaxeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaxe() throws Exception {
        // Initialize the database
        taxeRepository.saveAndFlush(taxe);

        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();

        // Update the taxe
        Taxe updatedTaxe = taxeRepository.findById(taxe.getId()).get();
        // Disconnect from session so that the updates on updatedTaxe are not directly saved in db
        em.detach(updatedTaxe);
        updatedTaxe.name(UPDATED_NAME).rate(UPDATED_RATE).type(UPDATED_TYPE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restTaxeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaxe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTaxe))
            )
            .andExpect(status().isOk());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
        Taxe testTaxe = taxeList.get(taxeList.size() - 1);
        assertThat(testTaxe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxe.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testTaxe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTaxe.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaxe.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTaxe() throws Exception {
        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();
        taxe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxe() throws Exception {
        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();
        taxe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxe() throws Exception {
        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();
        taxe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxeWithPatch() throws Exception {
        // Initialize the database
        taxeRepository.saveAndFlush(taxe);

        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();

        // Update the taxe using partial update
        Taxe partialUpdatedTaxe = new Taxe();
        partialUpdatedTaxe.setId(taxe.getId());

        partialUpdatedTaxe.rate(UPDATED_RATE).type(UPDATED_TYPE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restTaxeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxe))
            )
            .andExpect(status().isOk());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
        Taxe testTaxe = taxeList.get(taxeList.size() - 1);
        assertThat(testTaxe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxe.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testTaxe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTaxe.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaxe.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTaxeWithPatch() throws Exception {
        // Initialize the database
        taxeRepository.saveAndFlush(taxe);

        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();

        // Update the taxe using partial update
        Taxe partialUpdatedTaxe = new Taxe();
        partialUpdatedTaxe.setId(taxe.getId());

        partialUpdatedTaxe
            .name(UPDATED_NAME)
            .rate(UPDATED_RATE)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTaxeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxe))
            )
            .andExpect(status().isOk());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
        Taxe testTaxe = taxeList.get(taxeList.size() - 1);
        assertThat(testTaxe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxe.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testTaxe.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTaxe.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaxe.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTaxe() throws Exception {
        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();
        taxe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxe() throws Exception {
        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();
        taxe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxe() throws Exception {
        int databaseSizeBeforeUpdate = taxeRepository.findAll().size();
        taxe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taxe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Taxe in the database
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxe() throws Exception {
        // Initialize the database
        taxeRepository.saveAndFlush(taxe);

        int databaseSizeBeforeDelete = taxeRepository.findAll().size();

        // Delete the taxe
        restTaxeMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Taxe> taxeList = taxeRepository.findAll();
        assertThat(taxeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
