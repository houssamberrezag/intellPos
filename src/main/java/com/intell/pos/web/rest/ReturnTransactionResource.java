package com.intell.pos.web.rest;

import com.intell.pos.domain.ReturnTransaction;
import com.intell.pos.repository.ReturnTransactionRepository;
import com.intell.pos.service.ReturnTransactionService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.intell.pos.domain.ReturnTransaction}.
 */
@RestController
@RequestMapping("/api")
public class ReturnTransactionResource {

    private final Logger log = LoggerFactory.getLogger(ReturnTransactionResource.class);

    private static final String ENTITY_NAME = "returnTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReturnTransactionService returnTransactionService;

    private final ReturnTransactionRepository returnTransactionRepository;

    public ReturnTransactionResource(
        ReturnTransactionService returnTransactionService,
        ReturnTransactionRepository returnTransactionRepository
    ) {
        this.returnTransactionService = returnTransactionService;
        this.returnTransactionRepository = returnTransactionRepository;
    }

    /**
     * {@code POST  /return-transactions} : Create a new returnTransaction.
     *
     * @param returnTransaction the returnTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnTransaction, or with status {@code 400 (Bad Request)} if the returnTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/return-transactions")
    public ResponseEntity<ReturnTransaction> createReturnTransaction(@Valid @RequestBody ReturnTransaction returnTransaction)
        throws URISyntaxException {
        log.debug("REST request to save ReturnTransaction : {}", returnTransaction);
        if (returnTransaction.getId() != null) {
            throw new BadRequestAlertException("A new returnTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReturnTransaction result = returnTransactionService.save(returnTransaction);
        return ResponseEntity
            .created(new URI("/api/return-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /return-transactions/:id} : Updates an existing returnTransaction.
     *
     * @param id the id of the returnTransaction to save.
     * @param returnTransaction the returnTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnTransaction,
     * or with status {@code 400 (Bad Request)} if the returnTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/return-transactions/{id}")
    public ResponseEntity<ReturnTransaction> updateReturnTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReturnTransaction returnTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update ReturnTransaction : {}, {}", id, returnTransaction);
        if (returnTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReturnTransaction result = returnTransactionService.save(returnTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, returnTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /return-transactions/:id} : Partial updates given fields of an existing returnTransaction, field will ignore if it is null
     *
     * @param id the id of the returnTransaction to save.
     * @param returnTransaction the returnTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnTransaction,
     * or with status {@code 400 (Bad Request)} if the returnTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the returnTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/return-transactions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReturnTransaction> partialUpdateReturnTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReturnTransaction returnTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReturnTransaction partially : {}, {}", id, returnTransaction);
        if (returnTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnTransaction> result = returnTransactionService.partialUpdate(returnTransaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, returnTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /return-transactions} : get all the returnTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of returnTransactions in body.
     */
    @GetMapping("/return-transactions")
    public ResponseEntity<List<ReturnTransaction>> getAllReturnTransactions(Pageable pageable) {
        log.debug("REST request to get a page of ReturnTransactions");
        Page<ReturnTransaction> page = returnTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /return-transactions/:id} : get the "id" returnTransaction.
     *
     * @param id the id of the returnTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/return-transactions/{id}")
    public ResponseEntity<ReturnTransaction> getReturnTransaction(@PathVariable Long id) {
        log.debug("REST request to get ReturnTransaction : {}", id);
        Optional<ReturnTransaction> returnTransaction = returnTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnTransaction);
    }

    /**
     * {@code DELETE  /return-transactions/:id} : delete the "id" returnTransaction.
     *
     * @param id the id of the returnTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/return-transactions/{id}")
    public ResponseEntity<Void> deleteReturnTransaction(@PathVariable Long id) {
        log.debug("REST request to delete ReturnTransaction : {}", id);
        returnTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
