package com.intell.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intell.pos.IntegrationTest;
import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.TransactionTypes;
import com.intell.pos.repository.TransactionRepository;
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
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionResourceIT {

    private static final String DEFAULT_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NO = "BBBBBBBBBB";

    private static final TransactionTypes DEFAULT_TRANSACTION_TYPE = TransactionTypes.OPENING;
    private static final TransactionTypes UPDATED_TRANSACTION_TYPE = TransactionTypes.SELL;

    private static final Double DEFAULT_TOTAL_COST_PRICE = 1D;
    private static final Double UPDATED_TOTAL_COST_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Double DEFAULT_INVOICE_TAX = 1D;
    private static final Double UPDATED_INVOICE_TAX = 2D;

    private static final Double DEFAULT_TOTAL_TAX = 1D;
    private static final Double UPDATED_TOTAL_TAX = 2D;

    private static final Double DEFAULT_LABOR_COST = 1D;
    private static final Double UPDATED_LABOR_COST = 2D;

    private static final Double DEFAULT_NET_TOTAL = 1D;
    private static final Double UPDATED_NET_TOTAL = 2D;

    private static final Double DEFAULT_PAID = 1D;
    private static final Double UPDATED_PAID = 2D;

    private static final Double DEFAULT_CHANGE_AMOUNT = 1D;
    private static final Double UPDATED_CHANGE_AMOUNT = 2D;

    private static final String DEFAULT_RETURN_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_RETURN_INVOICE = "BBBBBBBBBB";

    private static final Double DEFAULT_RETURN_BALANCE = 1D;
    private static final Double UPDATED_RETURN_BALANCE = 2D;

    private static final Boolean DEFAULT_POS = false;
    private static final Boolean UPDATED_POS = true;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .referenceNo(DEFAULT_REFERENCE_NO)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .totalCostPrice(DEFAULT_TOTAL_COST_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .total(DEFAULT_TOTAL)
            .invoiceTax(DEFAULT_INVOICE_TAX)
            .totalTax(DEFAULT_TOTAL_TAX)
            .laborCost(DEFAULT_LABOR_COST)
            .netTotal(DEFAULT_NET_TOTAL)
            .paid(DEFAULT_PAID)
            .changeAmount(DEFAULT_CHANGE_AMOUNT)
            .returnInvoice(DEFAULT_RETURN_INVOICE)
            .returnBalance(DEFAULT_RETURN_BALANCE)
            .pos(DEFAULT_POS)
            .date(DEFAULT_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .referenceNo(UPDATED_REFERENCE_NO)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .totalCostPrice(UPDATED_TOTAL_COST_PRICE)
            .discount(UPDATED_DISCOUNT)
            .total(UPDATED_TOTAL)
            .invoiceTax(UPDATED_INVOICE_TAX)
            .totalTax(UPDATED_TOTAL_TAX)
            .laborCost(UPDATED_LABOR_COST)
            .netTotal(UPDATED_NET_TOTAL)
            .paid(UPDATED_PAID)
            .changeAmount(UPDATED_CHANGE_AMOUNT)
            .returnInvoice(UPDATED_RETURN_INVOICE)
            .returnBalance(UPDATED_RETURN_BALANCE)
            .pos(UPDATED_POS)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();
        // Create the Transaction
        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getReferenceNo()).isEqualTo(DEFAULT_REFERENCE_NO);
        assertThat(testTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testTransaction.getTotalCostPrice()).isEqualTo(DEFAULT_TOTAL_COST_PRICE);
        assertThat(testTransaction.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testTransaction.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testTransaction.getInvoiceTax()).isEqualTo(DEFAULT_INVOICE_TAX);
        assertThat(testTransaction.getTotalTax()).isEqualTo(DEFAULT_TOTAL_TAX);
        assertThat(testTransaction.getLaborCost()).isEqualTo(DEFAULT_LABOR_COST);
        assertThat(testTransaction.getNetTotal()).isEqualTo(DEFAULT_NET_TOTAL);
        assertThat(testTransaction.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testTransaction.getChangeAmount()).isEqualTo(DEFAULT_CHANGE_AMOUNT);
        assertThat(testTransaction.getReturnInvoice()).isEqualTo(DEFAULT_RETURN_INVOICE);
        assertThat(testTransaction.getReturnBalance()).isEqualTo(DEFAULT_RETURN_BALANCE);
        assertThat(testTransaction.getPos()).isEqualTo(DEFAULT_POS);
        assertThat(testTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTransaction.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);

        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setReferenceNo(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactionType(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setDiscount(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTotal(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLaborCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setLaborCost(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setPaid(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPosIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setPos(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalCostPrice").value(hasItem(DEFAULT_TOTAL_COST_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].invoiceTax").value(hasItem(DEFAULT_INVOICE_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTax").value(hasItem(DEFAULT_TOTAL_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].laborCost").value(hasItem(DEFAULT_LABOR_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].netTotal").value(hasItem(DEFAULT_NET_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].changeAmount").value(hasItem(DEFAULT_CHANGE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].returnInvoice").value(hasItem(DEFAULT_RETURN_INVOICE)))
            .andExpect(jsonPath("$.[*].returnBalance").value(hasItem(DEFAULT_RETURN_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].pos").value(hasItem(DEFAULT_POS.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }

    @Test
    @Transactional
    void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.referenceNo").value(DEFAULT_REFERENCE_NO))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.totalCostPrice").value(DEFAULT_TOTAL_COST_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.invoiceTax").value(DEFAULT_INVOICE_TAX.doubleValue()))
            .andExpect(jsonPath("$.totalTax").value(DEFAULT_TOTAL_TAX.doubleValue()))
            .andExpect(jsonPath("$.laborCost").value(DEFAULT_LABOR_COST.doubleValue()))
            .andExpect(jsonPath("$.netTotal").value(DEFAULT_NET_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.doubleValue()))
            .andExpect(jsonPath("$.changeAmount").value(DEFAULT_CHANGE_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.returnInvoice").value(DEFAULT_RETURN_INVOICE))
            .andExpect(jsonPath("$.returnBalance").value(DEFAULT_RETURN_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.pos").value(DEFAULT_POS.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .referenceNo(UPDATED_REFERENCE_NO)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .totalCostPrice(UPDATED_TOTAL_COST_PRICE)
            .discount(UPDATED_DISCOUNT)
            .total(UPDATED_TOTAL)
            .invoiceTax(UPDATED_INVOICE_TAX)
            .totalTax(UPDATED_TOTAL_TAX)
            .laborCost(UPDATED_LABOR_COST)
            .netTotal(UPDATED_NET_TOTAL)
            .paid(UPDATED_PAID)
            .changeAmount(UPDATED_CHANGE_AMOUNT)
            .returnInvoice(UPDATED_RETURN_INVOICE)
            .returnBalance(UPDATED_RETURN_BALANCE)
            .pos(UPDATED_POS)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getReferenceNo()).isEqualTo(UPDATED_REFERENCE_NO);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransaction.getTotalCostPrice()).isEqualTo(UPDATED_TOTAL_COST_PRICE);
        assertThat(testTransaction.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testTransaction.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testTransaction.getInvoiceTax()).isEqualTo(UPDATED_INVOICE_TAX);
        assertThat(testTransaction.getTotalTax()).isEqualTo(UPDATED_TOTAL_TAX);
        assertThat(testTransaction.getLaborCost()).isEqualTo(UPDATED_LABOR_COST);
        assertThat(testTransaction.getNetTotal()).isEqualTo(UPDATED_NET_TOTAL);
        assertThat(testTransaction.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testTransaction.getChangeAmount()).isEqualTo(UPDATED_CHANGE_AMOUNT);
        assertThat(testTransaction.getReturnInvoice()).isEqualTo(UPDATED_RETURN_INVOICE);
        assertThat(testTransaction.getReturnBalance()).isEqualTo(UPDATED_RETURN_BALANCE);
        assertThat(testTransaction.getPos()).isEqualTo(UPDATED_POS);
        assertThat(testTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTransaction.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .netTotal(UPDATED_NET_TOTAL)
            .paid(UPDATED_PAID)
            .changeAmount(UPDATED_CHANGE_AMOUNT)
            .returnInvoice(UPDATED_RETURN_INVOICE)
            .returnBalance(UPDATED_RETURN_BALANCE);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getReferenceNo()).isEqualTo(DEFAULT_REFERENCE_NO);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransaction.getTotalCostPrice()).isEqualTo(DEFAULT_TOTAL_COST_PRICE);
        assertThat(testTransaction.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testTransaction.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testTransaction.getInvoiceTax()).isEqualTo(DEFAULT_INVOICE_TAX);
        assertThat(testTransaction.getTotalTax()).isEqualTo(DEFAULT_TOTAL_TAX);
        assertThat(testTransaction.getLaborCost()).isEqualTo(DEFAULT_LABOR_COST);
        assertThat(testTransaction.getNetTotal()).isEqualTo(UPDATED_NET_TOTAL);
        assertThat(testTransaction.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testTransaction.getChangeAmount()).isEqualTo(UPDATED_CHANGE_AMOUNT);
        assertThat(testTransaction.getReturnInvoice()).isEqualTo(UPDATED_RETURN_INVOICE);
        assertThat(testTransaction.getReturnBalance()).isEqualTo(UPDATED_RETURN_BALANCE);
        assertThat(testTransaction.getPos()).isEqualTo(DEFAULT_POS);
        assertThat(testTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTransaction.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .referenceNo(UPDATED_REFERENCE_NO)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .totalCostPrice(UPDATED_TOTAL_COST_PRICE)
            .discount(UPDATED_DISCOUNT)
            .total(UPDATED_TOTAL)
            .invoiceTax(UPDATED_INVOICE_TAX)
            .totalTax(UPDATED_TOTAL_TAX)
            .laborCost(UPDATED_LABOR_COST)
            .netTotal(UPDATED_NET_TOTAL)
            .paid(UPDATED_PAID)
            .changeAmount(UPDATED_CHANGE_AMOUNT)
            .returnInvoice(UPDATED_RETURN_INVOICE)
            .returnBalance(UPDATED_RETURN_BALANCE)
            .pos(UPDATED_POS)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getReferenceNo()).isEqualTo(UPDATED_REFERENCE_NO);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testTransaction.getTotalCostPrice()).isEqualTo(UPDATED_TOTAL_COST_PRICE);
        assertThat(testTransaction.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testTransaction.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testTransaction.getInvoiceTax()).isEqualTo(UPDATED_INVOICE_TAX);
        assertThat(testTransaction.getTotalTax()).isEqualTo(UPDATED_TOTAL_TAX);
        assertThat(testTransaction.getLaborCost()).isEqualTo(UPDATED_LABOR_COST);
        assertThat(testTransaction.getNetTotal()).isEqualTo(UPDATED_NET_TOTAL);
        assertThat(testTransaction.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testTransaction.getChangeAmount()).isEqualTo(UPDATED_CHANGE_AMOUNT);
        assertThat(testTransaction.getReturnInvoice()).isEqualTo(UPDATED_RETURN_INVOICE);
        assertThat(testTransaction.getReturnBalance()).isEqualTo(UPDATED_RETURN_BALANCE);
        assertThat(testTransaction.getPos()).isEqualTo(UPDATED_POS);
        assertThat(testTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTransaction.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
