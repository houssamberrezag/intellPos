package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.ExpenseCategorie;
import com.intell.pos.repository.ExpenseCategorieRepository;
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
 * Integration tests for the {@link ExpenseCategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseCategorieResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/expense-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpenseCategorieRepository expenseCategorieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseCategorieMockMvc;

    private ExpenseCategorie expenseCategorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseCategorie createEntity(EntityManager em) {
        ExpenseCategorie expenseCategorie = new ExpenseCategorie()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return expenseCategorie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseCategorie createUpdatedEntity(EntityManager em) {
        ExpenseCategorie expenseCategorie = new ExpenseCategorie()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return expenseCategorie;
    }

    @BeforeEach
    public void initTest() {
        expenseCategorie = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenseCategorie() throws Exception {
        int databaseSizeBeforeCreate = expenseCategorieRepository.findAll().size();
        // Create the ExpenseCategorie
        restExpenseCategorieMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isCreated());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseCategorie testExpenseCategorie = expenseCategorieList.get(expenseCategorieList.size() - 1);
        assertThat(testExpenseCategorie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExpenseCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testExpenseCategorie.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createExpenseCategorieWithExistingId() throws Exception {
        // Create the ExpenseCategorie with an existing ID
        expenseCategorie.setId(1L);

        int databaseSizeBeforeCreate = expenseCategorieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseCategorieMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseCategorieRepository.findAll().size();
        // set the field null
        expenseCategorie.setName(null);

        // Create the ExpenseCategorie, which fails.

        restExpenseCategorieMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isBadRequest());

        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenseCategories() throws Exception {
        // Initialize the database
        expenseCategorieRepository.saveAndFlush(expenseCategorie);

        // Get all the expenseCategorieList
        restExpenseCategorieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseCategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getExpenseCategorie() throws Exception {
        // Initialize the database
        expenseCategorieRepository.saveAndFlush(expenseCategorie);

        // Get the expenseCategorie
        restExpenseCategorieMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseCategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseCategorie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExpenseCategorie() throws Exception {
        // Get the expenseCategorie
        restExpenseCategorieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExpenseCategorie() throws Exception {
        // Initialize the database
        expenseCategorieRepository.saveAndFlush(expenseCategorie);

        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();

        // Update the expenseCategorie
        ExpenseCategorie updatedExpenseCategorie = expenseCategorieRepository.findById(expenseCategorie.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseCategorie are not directly saved in db
        em.detach(updatedExpenseCategorie);
        updatedExpenseCategorie.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restExpenseCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenseCategorie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpenseCategorie))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
        ExpenseCategorie testExpenseCategorie = expenseCategorieList.get(expenseCategorieList.size() - 1);
        assertThat(testExpenseCategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExpenseCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testExpenseCategorie.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingExpenseCategorie() throws Exception {
        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();
        expenseCategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseCategorie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseCategorie() throws Exception {
        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();
        expenseCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseCategorie() throws Exception {
        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();
        expenseCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategorieMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseCategorieWithPatch() throws Exception {
        // Initialize the database
        expenseCategorieRepository.saveAndFlush(expenseCategorie);

        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();

        // Update the expenseCategorie using partial update
        ExpenseCategorie partialUpdatedExpenseCategorie = new ExpenseCategorie();
        partialUpdatedExpenseCategorie.setId(expenseCategorie.getId());

        partialUpdatedExpenseCategorie.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        restExpenseCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseCategorie))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
        ExpenseCategorie testExpenseCategorie = expenseCategorieList.get(expenseCategorieList.size() - 1);
        assertThat(testExpenseCategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExpenseCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testExpenseCategorie.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateExpenseCategorieWithPatch() throws Exception {
        // Initialize the database
        expenseCategorieRepository.saveAndFlush(expenseCategorie);

        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();

        // Update the expenseCategorie using partial update
        ExpenseCategorie partialUpdatedExpenseCategorie = new ExpenseCategorie();
        partialUpdatedExpenseCategorie.setId(expenseCategorie.getId());

        partialUpdatedExpenseCategorie.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restExpenseCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenseCategorie))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
        ExpenseCategorie testExpenseCategorie = expenseCategorieList.get(expenseCategorieList.size() - 1);
        assertThat(testExpenseCategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExpenseCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testExpenseCategorie.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingExpenseCategorie() throws Exception {
        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();
        expenseCategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseCategorie() throws Exception {
        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();
        expenseCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseCategorie() throws Exception {
        int databaseSizeBeforeUpdate = expenseCategorieRepository.findAll().size();
        expenseCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenseCategorie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseCategorie in the database
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenseCategorie() throws Exception {
        // Initialize the database
        expenseCategorieRepository.saveAndFlush(expenseCategorie);

        int databaseSizeBeforeDelete = expenseCategorieRepository.findAll().size();

        // Delete the expenseCategorie
        restExpenseCategorieMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseCategorie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseCategorie> expenseCategorieList = expenseCategorieRepository.findAll();
        assertThat(expenseCategorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
