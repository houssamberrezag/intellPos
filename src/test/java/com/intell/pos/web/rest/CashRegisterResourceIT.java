package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.CashRegister;
import com.intell.pos.repository.CashRegisterRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CashRegisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CashRegisterResourceIT {

    private static final Double DEFAULT_CASH_IN_HANDS = 1D;
    private static final Double UPDATED_CASH_IN_HANDS = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cash-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashRegisterMockMvc;

    private CashRegister cashRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashRegister createEntity(EntityManager em) {
        CashRegister cashRegister = new CashRegister()
            .cashInHands(DEFAULT_CASH_IN_HANDS)
            .date(DEFAULT_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return cashRegister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashRegister createUpdatedEntity(EntityManager em) {
        CashRegister cashRegister = new CashRegister()
            .cashInHands(UPDATED_CASH_IN_HANDS)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return cashRegister;
    }

    @BeforeEach
    public void initTest() {
        cashRegister = createEntity(em);
    }

    @Test
    @Transactional
    void createCashRegister() throws Exception {
        int databaseSizeBeforeCreate = cashRegisterRepository.findAll().size();
        // Create the CashRegister
        restCashRegisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegister)))
            .andExpect(status().isCreated());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeCreate + 1);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCashInHands()).isEqualTo(DEFAULT_CASH_IN_HANDS);
        assertThat(testCashRegister.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCashRegister.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCashRegister.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createCashRegisterWithExistingId() throws Exception {
        // Create the CashRegister with an existing ID
        cashRegister.setId(1L);

        int databaseSizeBeforeCreate = cashRegisterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashRegisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegister)))
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCashInHandsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashRegisterRepository.findAll().size();
        // set the field null
        cashRegister.setCashInHands(null);

        // Create the CashRegister, which fails.

        restCashRegisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegister)))
            .andExpect(status().isBadRequest());

        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashRegisterRepository.findAll().size();
        // set the field null
        cashRegister.setDate(null);

        // Create the CashRegister, which fails.

        restCashRegisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegister)))
            .andExpect(status().isBadRequest());

        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCashRegisters() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        // Get all the cashRegisterList
        restCashRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].cashInHands").value(hasItem(DEFAULT_CASH_IN_HANDS.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCashRegister() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        // Get the cashRegister
        restCashRegisterMockMvc
            .perform(get(ENTITY_API_URL_ID, cashRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashRegister.getId().intValue()))
            .andExpect(jsonPath("$.cashInHands").value(DEFAULT_CASH_IN_HANDS.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCashRegister() throws Exception {
        // Get the cashRegister
        restCashRegisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCashRegister() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();

        // Update the cashRegister
        CashRegister updatedCashRegister = cashRegisterRepository.findById(cashRegister.getId()).get();
        // Disconnect from session so that the updates on updatedCashRegister are not directly saved in db
        em.detach(updatedCashRegister);
        updatedCashRegister
            .cashInHands(UPDATED_CASH_IN_HANDS)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCashRegister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCashRegister))
            )
            .andExpect(status().isOk());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCashInHands()).isEqualTo(UPDATED_CASH_IN_HANDS);
        assertThat(testCashRegister.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCashRegister.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCashRegister.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashRegister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegister)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashRegisterWithPatch() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();

        // Update the cashRegister using partial update
        CashRegister partialUpdatedCashRegister = new CashRegister();
        partialUpdatedCashRegister.setId(cashRegister.getId());

        partialUpdatedCashRegister.cashInHands(UPDATED_CASH_IN_HANDS);

        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashRegister))
            )
            .andExpect(status().isOk());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCashInHands()).isEqualTo(UPDATED_CASH_IN_HANDS);
        assertThat(testCashRegister.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCashRegister.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCashRegister.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateCashRegisterWithPatch() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();

        // Update the cashRegister using partial update
        CashRegister partialUpdatedCashRegister = new CashRegister();
        partialUpdatedCashRegister.setId(cashRegister.getId());

        partialUpdatedCashRegister
            .cashInHands(UPDATED_CASH_IN_HANDS)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashRegister))
            )
            .andExpect(status().isOk());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCashInHands()).isEqualTo(UPDATED_CASH_IN_HANDS);
        assertThat(testCashRegister.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCashRegister.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCashRegister.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cashRegister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashRegister() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeDelete = cashRegisterRepository.findAll().size();

        // Delete the cashRegister
        restCashRegisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashRegister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
