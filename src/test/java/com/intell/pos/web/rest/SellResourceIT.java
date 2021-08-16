package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.Sell;
import com.intell.pos.repository.SellRepository;
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
 * Integration tests for the {@link SellResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellResourceIT {

    private static final String DEFAULT_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NO = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final Double DEFAULT_UNIT_COST_PRICE = 1D;
    private static final Double UPDATED_UNIT_COST_PRICE = 2D;

    private static final Double DEFAULT_SUB_TOTAL = 1D;
    private static final Double UPDATED_SUB_TOTAL = 2D;

    private static final Double DEFAULT_PRODUCT_TAX = 1D;
    private static final Double UPDATED_PRODUCT_TAX = 2D;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sells";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellMockMvc;

    private Sell sell;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sell createEntity(EntityManager em) {
        Sell sell = new Sell()
            .referenceNo(DEFAULT_REFERENCE_NO)
            .quantity(DEFAULT_QUANTITY)
            .unitCostPrice(DEFAULT_UNIT_COST_PRICE)
            .subTotal(DEFAULT_SUB_TOTAL)
            .productTax(DEFAULT_PRODUCT_TAX)
            .date(DEFAULT_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return sell;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sell createUpdatedEntity(EntityManager em) {
        Sell sell = new Sell()
            .referenceNo(UPDATED_REFERENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .unitCostPrice(UPDATED_UNIT_COST_PRICE)
            .subTotal(UPDATED_SUB_TOTAL)
            .productTax(UPDATED_PRODUCT_TAX)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return sell;
    }

    @BeforeEach
    public void initTest() {
        sell = createEntity(em);
    }

    @Test
    @Transactional
    void createSell() throws Exception {
        int databaseSizeBeforeCreate = sellRepository.findAll().size();
        // Create the Sell
        restSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isCreated());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeCreate + 1);
        Sell testSell = sellList.get(sellList.size() - 1);
        assertThat(testSell.getReferenceNo()).isEqualTo(DEFAULT_REFERENCE_NO);
        assertThat(testSell.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSell.getUnitCostPrice()).isEqualTo(DEFAULT_UNIT_COST_PRICE);
        assertThat(testSell.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testSell.getProductTax()).isEqualTo(DEFAULT_PRODUCT_TAX);
        assertThat(testSell.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSell.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSell.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSell.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void createSellWithExistingId() throws Exception {
        // Create the Sell with an existing ID
        sell.setId(1L);

        int databaseSizeBeforeCreate = sellRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isBadRequest());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellRepository.findAll().size();
        // set the field null
        sell.setReferenceNo(null);

        // Create the Sell, which fails.

        restSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isBadRequest());

        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellRepository.findAll().size();
        // set the field null
        sell.setQuantity(null);

        // Create the Sell, which fails.

        restSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isBadRequest());

        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellRepository.findAll().size();
        // set the field null
        sell.setSubTotal(null);

        // Create the Sell, which fails.

        restSellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isBadRequest());

        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSells() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        // Get all the sellList
        restSellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sell.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].unitCostPrice").value(hasItem(DEFAULT_UNIT_COST_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].productTax").value(hasItem(DEFAULT_PRODUCT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }

    @Test
    @Transactional
    void getSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        // Get the sell
        restSellMockMvc
            .perform(get(ENTITY_API_URL_ID, sell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sell.getId().intValue()))
            .andExpect(jsonPath("$.referenceNo").value(DEFAULT_REFERENCE_NO))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.unitCostPrice").value(DEFAULT_UNIT_COST_PRICE.doubleValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.productTax").value(DEFAULT_PRODUCT_TAX.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSell() throws Exception {
        // Get the sell
        restSellMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        int databaseSizeBeforeUpdate = sellRepository.findAll().size();

        // Update the sell
        Sell updatedSell = sellRepository.findById(sell.getId()).get();
        // Disconnect from session so that the updates on updatedSell are not directly saved in db
        em.detach(updatedSell);
        updatedSell
            .referenceNo(UPDATED_REFERENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .unitCostPrice(UPDATED_UNIT_COST_PRICE)
            .subTotal(UPDATED_SUB_TOTAL)
            .productTax(UPDATED_PRODUCT_TAX)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restSellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSell))
            )
            .andExpect(status().isOk());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
        Sell testSell = sellList.get(sellList.size() - 1);
        assertThat(testSell.getReferenceNo()).isEqualTo(UPDATED_REFERENCE_NO);
        assertThat(testSell.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSell.getUnitCostPrice()).isEqualTo(UPDATED_UNIT_COST_PRICE);
        assertThat(testSell.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testSell.getProductTax()).isEqualTo(UPDATED_PRODUCT_TAX);
        assertThat(testSell.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSell.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSell.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSell.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();
        sell.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();
        sell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();
        sell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSellWithPatch() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        int databaseSizeBeforeUpdate = sellRepository.findAll().size();

        // Update the sell using partial update
        Sell partialUpdatedSell = new Sell();
        partialUpdatedSell.setId(sell.getId());

        partialUpdatedSell.productTax(UPDATED_PRODUCT_TAX).date(UPDATED_DATE);

        restSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSell.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSell))
            )
            .andExpect(status().isOk());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
        Sell testSell = sellList.get(sellList.size() - 1);
        assertThat(testSell.getReferenceNo()).isEqualTo(DEFAULT_REFERENCE_NO);
        assertThat(testSell.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSell.getUnitCostPrice()).isEqualTo(DEFAULT_UNIT_COST_PRICE);
        assertThat(testSell.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testSell.getProductTax()).isEqualTo(UPDATED_PRODUCT_TAX);
        assertThat(testSell.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSell.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSell.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSell.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSellWithPatch() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        int databaseSizeBeforeUpdate = sellRepository.findAll().size();

        // Update the sell using partial update
        Sell partialUpdatedSell = new Sell();
        partialUpdatedSell.setId(sell.getId());

        partialUpdatedSell
            .referenceNo(UPDATED_REFERENCE_NO)
            .quantity(UPDATED_QUANTITY)
            .unitCostPrice(UPDATED_UNIT_COST_PRICE)
            .subTotal(UPDATED_SUB_TOTAL)
            .productTax(UPDATED_PRODUCT_TAX)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSell.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSell))
            )
            .andExpect(status().isOk());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
        Sell testSell = sellList.get(sellList.size() - 1);
        assertThat(testSell.getReferenceNo()).isEqualTo(UPDATED_REFERENCE_NO);
        assertThat(testSell.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSell.getUnitCostPrice()).isEqualTo(UPDATED_UNIT_COST_PRICE);
        assertThat(testSell.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testSell.getProductTax()).isEqualTo(UPDATED_PRODUCT_TAX);
        assertThat(testSell.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSell.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSell.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSell.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();
        sell.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sell.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();
        sell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSell() throws Exception {
        int databaseSizeBeforeUpdate = sellRepository.findAll().size();
        sell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sell in the database
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSell() throws Exception {
        // Initialize the database
        sellRepository.saveAndFlush(sell);

        int databaseSizeBeforeDelete = sellRepository.findAll().size();

        // Delete the sell
        restSellMockMvc
            .perform(delete(ENTITY_API_URL_ID, sell.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sell> sellList = sellRepository.findAll();
        assertThat(sellList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
