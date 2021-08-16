package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.ReturnTransaction;
import com.intell.pos.repository.ReturnTransactionRepository;
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
 * Integration tests for the {@link ReturnTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReturnTransactionResourceIT {

    private static final Double DEFAULT_RETURN_VAT = 1D;
    private static final Double UPDATED_RETURN_VAT = 2D;

    private static final String DEFAULT_SELLS_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_SELLS_REFERENCE_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_RETURN_UNITS = 1;
    private static final Integer UPDATED_RETURN_UNITS = 2;

    private static final Double DEFAULT_RETURN_AMOUNT = 1D;
    private static final Double UPDATED_RETURN_AMOUNT = 2D;

    private static final Integer DEFAULT_RETURNED_BY = 1;
    private static final Integer UPDATED_RETURNED_BY = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/return-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReturnTransactionRepository returnTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnTransactionMockMvc;

    private ReturnTransaction returnTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnTransaction createEntity(EntityManager em) {
        ReturnTransaction returnTransaction = new ReturnTransaction()
            .returnVat(DEFAULT_RETURN_VAT)
            .sellsReferenceNo(DEFAULT_SELLS_REFERENCE_NO)
            .returnUnits(DEFAULT_RETURN_UNITS)
            .returnAmount(DEFAULT_RETURN_AMOUNT)
            .returnedBy(DEFAULT_RETURNED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return returnTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnTransaction createUpdatedEntity(EntityManager em) {
        ReturnTransaction returnTransaction = new ReturnTransaction()
            .returnVat(UPDATED_RETURN_VAT)
            .sellsReferenceNo(UPDATED_SELLS_REFERENCE_NO)
            .returnUnits(UPDATED_RETURN_UNITS)
            .returnAmount(UPDATED_RETURN_AMOUNT)
            .returnedBy(UPDATED_RETURNED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return returnTransaction;
    }

    @BeforeEach
    public void initTest() {
        returnTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createReturnTransaction() throws Exception {
        int databaseSizeBeforeCreate = returnTransactionRepository.findAll().size();
        // Create the ReturnTransaction
        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isCreated());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        ReturnTransaction testReturnTransaction = returnTransactionList.get(returnTransactionList.size() - 1);
        assertThat(testReturnTransaction.getReturnVat()).isEqualTo(DEFAULT_RETURN_VAT);
        assertThat(testReturnTransaction.getSellsReferenceNo()).isEqualTo(DEFAULT_SELLS_REFERENCE_NO);
        assertThat(testReturnTransaction.getReturnUnits()).isEqualTo(DEFAULT_RETURN_UNITS);
        assertThat(testReturnTransaction.getReturnAmount()).isEqualTo(DEFAULT_RETURN_AMOUNT);
        assertThat(testReturnTransaction.getReturnedBy()).isEqualTo(DEFAULT_RETURNED_BY);
        assertThat(testReturnTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReturnTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testReturnTransaction.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void createReturnTransactionWithExistingId() throws Exception {
        // Create the ReturnTransaction with an existing ID
        returnTransaction.setId(1L);

        int databaseSizeBeforeCreate = returnTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReturnVatIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setReturnVat(null);

        // Create the ReturnTransaction, which fails.

        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSellsReferenceNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setSellsReferenceNo(null);

        // Create the ReturnTransaction, which fails.

        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReturnUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setReturnUnits(null);

        // Create the ReturnTransaction, which fails.

        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReturnAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setReturnAmount(null);

        // Create the ReturnTransaction, which fails.

        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReturnedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = returnTransactionRepository.findAll().size();
        // set the field null
        returnTransaction.setReturnedBy(null);

        // Create the ReturnTransaction, which fails.

        restReturnTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReturnTransactions() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get all the returnTransactionList
        restReturnTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].returnVat").value(hasItem(DEFAULT_RETURN_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].sellsReferenceNo").value(hasItem(DEFAULT_SELLS_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].returnUnits").value(hasItem(DEFAULT_RETURN_UNITS)))
            .andExpect(jsonPath("$.[*].returnAmount").value(hasItem(DEFAULT_RETURN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].returnedBy").value(hasItem(DEFAULT_RETURNED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }

    @Test
    @Transactional
    void getReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        // Get the returnTransaction
        restReturnTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, returnTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnTransaction.getId().intValue()))
            .andExpect(jsonPath("$.returnVat").value(DEFAULT_RETURN_VAT.doubleValue()))
            .andExpect(jsonPath("$.sellsReferenceNo").value(DEFAULT_SELLS_REFERENCE_NO))
            .andExpect(jsonPath("$.returnUnits").value(DEFAULT_RETURN_UNITS))
            .andExpect(jsonPath("$.returnAmount").value(DEFAULT_RETURN_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.returnedBy").value(DEFAULT_RETURNED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReturnTransaction() throws Exception {
        // Get the returnTransaction
        restReturnTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();

        // Update the returnTransaction
        ReturnTransaction updatedReturnTransaction = returnTransactionRepository.findById(returnTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedReturnTransaction are not directly saved in db
        em.detach(updatedReturnTransaction);
        updatedReturnTransaction
            .returnVat(UPDATED_RETURN_VAT)
            .sellsReferenceNo(UPDATED_SELLS_REFERENCE_NO)
            .returnUnits(UPDATED_RETURN_UNITS)
            .returnAmount(UPDATED_RETURN_AMOUNT)
            .returnedBy(UPDATED_RETURNED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restReturnTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReturnTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
        ReturnTransaction testReturnTransaction = returnTransactionList.get(returnTransactionList.size() - 1);
        assertThat(testReturnTransaction.getReturnVat()).isEqualTo(UPDATED_RETURN_VAT);
        assertThat(testReturnTransaction.getSellsReferenceNo()).isEqualTo(UPDATED_SELLS_REFERENCE_NO);
        assertThat(testReturnTransaction.getReturnUnits()).isEqualTo(UPDATED_RETURN_UNITS);
        assertThat(testReturnTransaction.getReturnAmount()).isEqualTo(UPDATED_RETURN_AMOUNT);
        assertThat(testReturnTransaction.getReturnedBy()).isEqualTo(UPDATED_RETURNED_BY);
        assertThat(testReturnTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReturnTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testReturnTransaction.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void putNonExistingReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();
        returnTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();
        returnTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();
        returnTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnTransactionWithPatch() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();

        // Update the returnTransaction using partial update
        ReturnTransaction partialUpdatedReturnTransaction = new ReturnTransaction();
        partialUpdatedReturnTransaction.setId(returnTransaction.getId());

        partialUpdatedReturnTransaction
            .returnVat(UPDATED_RETURN_VAT)
            .returnAmount(UPDATED_RETURN_AMOUNT)
            .returnedBy(UPDATED_RETURNED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restReturnTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReturnTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
        ReturnTransaction testReturnTransaction = returnTransactionList.get(returnTransactionList.size() - 1);
        assertThat(testReturnTransaction.getReturnVat()).isEqualTo(UPDATED_RETURN_VAT);
        assertThat(testReturnTransaction.getSellsReferenceNo()).isEqualTo(DEFAULT_SELLS_REFERENCE_NO);
        assertThat(testReturnTransaction.getReturnUnits()).isEqualTo(DEFAULT_RETURN_UNITS);
        assertThat(testReturnTransaction.getReturnAmount()).isEqualTo(UPDATED_RETURN_AMOUNT);
        assertThat(testReturnTransaction.getReturnedBy()).isEqualTo(UPDATED_RETURNED_BY);
        assertThat(testReturnTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReturnTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testReturnTransaction.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void fullUpdateReturnTransactionWithPatch() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();

        // Update the returnTransaction using partial update
        ReturnTransaction partialUpdatedReturnTransaction = new ReturnTransaction();
        partialUpdatedReturnTransaction.setId(returnTransaction.getId());

        partialUpdatedReturnTransaction
            .returnVat(UPDATED_RETURN_VAT)
            .sellsReferenceNo(UPDATED_SELLS_REFERENCE_NO)
            .returnUnits(UPDATED_RETURN_UNITS)
            .returnAmount(UPDATED_RETURN_AMOUNT)
            .returnedBy(UPDATED_RETURNED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restReturnTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReturnTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
        ReturnTransaction testReturnTransaction = returnTransactionList.get(returnTransactionList.size() - 1);
        assertThat(testReturnTransaction.getReturnVat()).isEqualTo(UPDATED_RETURN_VAT);
        assertThat(testReturnTransaction.getSellsReferenceNo()).isEqualTo(UPDATED_SELLS_REFERENCE_NO);
        assertThat(testReturnTransaction.getReturnUnits()).isEqualTo(UPDATED_RETURN_UNITS);
        assertThat(testReturnTransaction.getReturnAmount()).isEqualTo(UPDATED_RETURN_AMOUNT);
        assertThat(testReturnTransaction.getReturnedBy()).isEqualTo(UPDATED_RETURNED_BY);
        assertThat(testReturnTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReturnTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testReturnTransaction.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();
        returnTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();
        returnTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnTransaction() throws Exception {
        int databaseSizeBeforeUpdate = returnTransactionRepository.findAll().size();
        returnTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnTransaction in the database
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnTransaction() throws Exception {
        // Initialize the database
        returnTransactionRepository.saveAndFlush(returnTransaction);

        int databaseSizeBeforeDelete = returnTransactionRepository.findAll().size();

        // Delete the returnTransaction
        restReturnTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReturnTransaction> returnTransactionList = returnTransactionRepository.findAll();
        assertThat(returnTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
