package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.Subcategorie;
import com.intell.pos.repository.SubcategorieRepository;
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
 * Integration tests for the {@link SubcategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubcategorieResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/subcategories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubcategorieRepository subcategorieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubcategorieMockMvc;

    private Subcategorie subcategorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subcategorie createEntity(EntityManager em) {
        Subcategorie subcategorie = new Subcategorie()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return subcategorie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subcategorie createUpdatedEntity(EntityManager em) {
        Subcategorie subcategorie = new Subcategorie()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return subcategorie;
    }

    @BeforeEach
    public void initTest() {
        subcategorie = createEntity(em);
    }

    @Test
    @Transactional
    void createSubcategorie() throws Exception {
        int databaseSizeBeforeCreate = subcategorieRepository.findAll().size();
        // Create the Subcategorie
        restSubcategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subcategorie)))
            .andExpect(status().isCreated());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeCreate + 1);
        Subcategorie testSubcategorie = subcategorieList.get(subcategorieList.size() - 1);
        assertThat(testSubcategorie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubcategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubcategorie.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSubcategorie.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void createSubcategorieWithExistingId() throws Exception {
        // Create the Subcategorie with an existing ID
        subcategorie.setId(1L);

        int databaseSizeBeforeCreate = subcategorieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubcategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subcategorie)))
            .andExpect(status().isBadRequest());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subcategorieRepository.findAll().size();
        // set the field null
        subcategorie.setName(null);

        // Create the Subcategorie, which fails.

        restSubcategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subcategorie)))
            .andExpect(status().isBadRequest());

        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubcategories() throws Exception {
        // Initialize the database
        subcategorieRepository.saveAndFlush(subcategorie);

        // Get all the subcategorieList
        restSubcategorieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subcategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }

    @Test
    @Transactional
    void getSubcategorie() throws Exception {
        // Initialize the database
        subcategorieRepository.saveAndFlush(subcategorie);

        // Get the subcategorie
        restSubcategorieMockMvc
            .perform(get(ENTITY_API_URL_ID, subcategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subcategorie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubcategorie() throws Exception {
        // Get the subcategorie
        restSubcategorieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubcategorie() throws Exception {
        // Initialize the database
        subcategorieRepository.saveAndFlush(subcategorie);

        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();

        // Update the subcategorie
        Subcategorie updatedSubcategorie = subcategorieRepository.findById(subcategorie.getId()).get();
        // Disconnect from session so that the updates on updatedSubcategorie are not directly saved in db
        em.detach(updatedSubcategorie);
        updatedSubcategorie.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).deletedAt(UPDATED_DELETED_AT);

        restSubcategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubcategorie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubcategorie))
            )
            .andExpect(status().isOk());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
        Subcategorie testSubcategorie = subcategorieList.get(subcategorieList.size() - 1);
        assertThat(testSubcategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubcategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubcategorie.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubcategorie.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSubcategorie() throws Exception {
        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();
        subcategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubcategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subcategorie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubcategorie() throws Exception {
        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();
        subcategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubcategorie() throws Exception {
        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();
        subcategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcategorieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subcategorie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubcategorieWithPatch() throws Exception {
        // Initialize the database
        subcategorieRepository.saveAndFlush(subcategorie);

        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();

        // Update the subcategorie using partial update
        Subcategorie partialUpdatedSubcategorie = new Subcategorie();
        partialUpdatedSubcategorie.setId(subcategorie.getId());

        partialUpdatedSubcategorie
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restSubcategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubcategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubcategorie))
            )
            .andExpect(status().isOk());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
        Subcategorie testSubcategorie = subcategorieList.get(subcategorieList.size() - 1);
        assertThat(testSubcategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubcategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubcategorie.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubcategorie.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSubcategorieWithPatch() throws Exception {
        // Initialize the database
        subcategorieRepository.saveAndFlush(subcategorie);

        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();

        // Update the subcategorie using partial update
        Subcategorie partialUpdatedSubcategorie = new Subcategorie();
        partialUpdatedSubcategorie.setId(subcategorie.getId());

        partialUpdatedSubcategorie
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restSubcategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubcategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubcategorie))
            )
            .andExpect(status().isOk());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
        Subcategorie testSubcategorie = subcategorieList.get(subcategorieList.size() - 1);
        assertThat(testSubcategorie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubcategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubcategorie.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubcategorie.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSubcategorie() throws Exception {
        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();
        subcategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubcategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subcategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subcategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubcategorie() throws Exception {
        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();
        subcategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subcategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubcategorie() throws Exception {
        int databaseSizeBeforeUpdate = subcategorieRepository.findAll().size();
        subcategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubcategorieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subcategorie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subcategorie in the database
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubcategorie() throws Exception {
        // Initialize the database
        subcategorieRepository.saveAndFlush(subcategorie);

        int databaseSizeBeforeDelete = subcategorieRepository.findAll().size();

        // Delete the subcategorie
        restSubcategorieMockMvc
            .perform(delete(ENTITY_API_URL_ID, subcategorie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subcategorie> subcategorieList = subcategorieRepository.findAll();
        assertThat(subcategorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
