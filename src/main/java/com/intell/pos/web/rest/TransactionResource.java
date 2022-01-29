package com.intell.pos.web.rest;

import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.TransactionTypes;
import com.intell.pos.domain.projection.ITransactionResume;
import com.intell.pos.repository.TransactionRepository;
import com.intell.pos.service.TransactionService;
import com.intell.pos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.intell.pos.domain.Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    public TransactionResource(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @param transaction the transaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaction, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transaction);
        if (transaction.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transaction result = transactionService.save(transaction);
        return ResponseEntity
            .created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions/:id} : Updates an existing transaction.
     *
     * @param id the id of the transaction to save.
     * @param transaction the transaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaction,
     * or with status {@code 400 (Bad Request)} if the transaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions/{id}")
    public ResponseEntity<Transaction> updateTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}, {}", id, transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transaction result = transactionService.save(transaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transactions/:id} : Partial updates given fields of an existing transaction, field will ignore if it is null
     *
     * @param id the id of the transaction to save.
     * @param transaction the transaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaction,
     * or with status {@code 400 (Bad Request)} if the transaction is not valid,
     * or with status {@code 404 (Not Found)} if the transaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the transaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transactions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Transaction> partialUpdateTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaction partially : {}, {}", id, transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Transaction> result = transactionService.partialUpdate(transaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transaction.getId().toString())
        );
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(Pageable pageable) {
        log.debug("REST request to get a page of Transactions");
        Page<Transaction> page = transactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<Transaction> transaction = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactionByRef")
    public ResponseEntity<Transaction> getTransactionByRef(@RequestParam String ref) {
        log.debug("REST request to get Transaction : {}", ref);
        Optional<Transaction> transaction = Optional.of(transactionService.findByRef(ref));
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transaction.
     *
     * @param id the id of the transaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /transactions} : get sell transactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions/sells")
    public ResponseEntity<List<Transaction>> getSellTransactions(Pageable pageable) {
        log.debug("REST request to get a page of sell Transactions");
        Page<Transaction> page = transactionService.findByTransactiontype(TransactionTypes.SELL, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions} : get purchases transactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions/purchases")
    public ResponseEntity<List<Transaction>> getPurchasesTransactions(Pageable pageable) {
        log.debug("REST request to get a page of parchases Transactions");
        Page<Transaction> page = transactionService.findByTransactiontype(TransactionTypes.PURCHASE, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions} : get today  sell transactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions/sells/today")
    public ResponseEntity<List<Transaction>> getTodaySellTransactions(Pageable pageable) {
        log.debug("REST request to get a page of today sell Transactions");
        Page<Transaction> page = transactionService.findTodayTransactionsByTransactiontype(TransactionTypes.SELL, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions} : get today purchases transactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions/purchases/today")
    public ResponseEntity<List<Transaction>> getTodayPurchasesTransactions(Pageable pageable) {
        log.debug("REST request to get a page of today purchases Transactions");
        Page<Transaction> page = transactionService.findTodayTransactionsByTransactiontype(TransactionTypes.PURCHASE, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions} : get sell transactions by person id.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions/sellsByPersonId")
    public ResponseEntity<List<Transaction>> getSellTransactionsByPersonId(Pageable pageable, @RequestParam Long personId) {
        log.debug("REST request to get a page of Transactions");
        Page<Transaction> page = transactionService.findByTransactiontypeAndPersonId(TransactionTypes.SELL, personId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions} : get purchases transactions by person Id.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions/purchasesByPersonId")
    public ResponseEntity<List<Transaction>> getPurchasesTransactionsByPersonId(Pageable pageable, @RequestParam Long personId) {
        log.debug("REST request to get a page of Transactions");
        Page<Transaction> page = transactionService.findByTransactiontypeAndPersonId(TransactionTypes.PURCHASE, personId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/transactions/purchaseByProductId")
    public ResponseEntity<List<Transaction>> getPurchasesTransactionsByProductId(Pageable pageable, @RequestParam Long productId) {
        log.debug("REST request to get a page of Transactions");
        Page<Transaction> page = transactionService.findByProductIdTypePurchase(productId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/transactions/sellByProductId")
    public ResponseEntity<List<Transaction>> getSellsTransactionsByProductId(Pageable pageable, @RequestParam Long productId) {
        log.debug("REST request to get a page of Transactions");
        Page<Transaction> page = transactionService.findByProductIdTypeSell(productId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/transactions/countByPersonId")
    public ResponseEntity<Integer> countByPersonId(@RequestParam Long personId) {
        log.debug("REST request to get a count of Transactions by person id");
        return ResponseEntity.ok(transactionService.countByPersonId(personId));
    }

    @GetMapping("/transactions/countByProductId")
    public ResponseEntity<Integer> countByProductId(@RequestParam Long productId) {
        log.debug("REST request to get a count of Transactions by product id");
        return ResponseEntity.ok(transactionService.countByProductId(productId));
    }

    @GetMapping("/transactions/totalAmountByPersonId")
    public ResponseEntity<Double> totalAmountByPersonId(@RequestParam Long personId) {
        log.debug("REST request to get a sum of amout of  Transactions by person id");
        return ResponseEntity.ok(transactionService.totalAmountByPersonId(personId));
    }

    @GetMapping("/transactions/sellResume")
    public ResponseEntity<ITransactionResume> sellResume() {
        log.debug("REST request to get a sum of amout of  Transactions by person id");
        return ResponseEntity.ok(transactionService.findResumeByTransactionType(TransactionTypes.SELL));
    }

    @GetMapping("/transactions/sellTodayResume")
    public ResponseEntity<ITransactionResume> sellTodayResume() {
        log.debug("REST request to get a sum of amout of  Transactions by person id");
        return ResponseEntity.ok(transactionService.findTodayResumeByTransactionType(TransactionTypes.SELL));
    }

    @GetMapping("/transactions/purchasesResume")
    public ResponseEntity<ITransactionResume> purchaseResume() {
        log.debug("REST request to get a sum of amout of  Transactions by person id");
        return ResponseEntity.ok(transactionService.findResumeByTransactionType(TransactionTypes.PURCHASE));
    }

    @GetMapping("/transactions/purchasesTodayResume")
    public ResponseEntity<ITransactionResume> purchaseTodayResume() {
        log.debug("REST request to get a sum of amout of  Transactions by person id");
        return ResponseEntity.ok(transactionService.findTodayResumeByTransactionType(TransactionTypes.PURCHASE));
    }
}
