package com.intell.pos.web.rest;

import com.intell.pos.domain.ExpenseCategorie;
import com.intell.pos.repository.ExpenseCategorieRepository;
import com.intell.pos.service.ExpenseCategorieService;
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
 * REST controller for managing {@link com.intell.pos.domain.ExpenseCategorie}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseCategorieResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseCategorieResource.class);

    private static final String ENTITY_NAME = "expenseCategorie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseCategorieService expenseCategorieService;

    private final ExpenseCategorieRepository expenseCategorieRepository;

    public ExpenseCategorieResource(
        ExpenseCategorieService expenseCategorieService,
        ExpenseCategorieRepository expenseCategorieRepository
    ) {
        this.expenseCategorieService = expenseCategorieService;
        this.expenseCategorieRepository = expenseCategorieRepository;
    }

    /**
     * {@code POST  /expense-categories} : Create a new expenseCategorie.
     *
     * @param expenseCategorie the expenseCategorie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseCategorie, or with status {@code 400 (Bad Request)} if the expenseCategorie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-categories")
    public ResponseEntity<ExpenseCategorie> createExpenseCategorie(@Valid @RequestBody ExpenseCategorie expenseCategorie)
        throws URISyntaxException {
        log.debug("REST request to save ExpenseCategorie : {}", expenseCategorie);
        if (expenseCategorie.getId() != null) {
            throw new BadRequestAlertException("A new expenseCategorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseCategorie result = expenseCategorieService.save(expenseCategorie);
        return ResponseEntity
            .created(new URI("/api/expense-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-categories/:id} : Updates an existing expenseCategorie.
     *
     * @param id the id of the expenseCategorie to save.
     * @param expenseCategorie the expenseCategorie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseCategorie,
     * or with status {@code 400 (Bad Request)} if the expenseCategorie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseCategorie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-categories/{id}")
    public ResponseEntity<ExpenseCategorie> updateExpenseCategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpenseCategorie expenseCategorie
    ) throws URISyntaxException {
        log.debug("REST request to update ExpenseCategorie : {}, {}", id, expenseCategorie);
        if (expenseCategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseCategorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseCategorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpenseCategorie result = expenseCategorieService.save(expenseCategorie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseCategorie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /expense-categories/:id} : Partial updates given fields of an existing expenseCategorie, field will ignore if it is null
     *
     * @param id the id of the expenseCategorie to save.
     * @param expenseCategorie the expenseCategorie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseCategorie,
     * or with status {@code 400 (Bad Request)} if the expenseCategorie is not valid,
     * or with status {@code 404 (Not Found)} if the expenseCategorie is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseCategorie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expense-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExpenseCategorie> partialUpdateExpenseCategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpenseCategorie expenseCategorie
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpenseCategorie partially : {}, {}", id, expenseCategorie);
        if (expenseCategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseCategorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseCategorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseCategorie> result = expenseCategorieService.partialUpdate(expenseCategorie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseCategorie.getId().toString())
        );
    }

    /**
     * {@code GET  /expense-categories} : get all the expenseCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseCategories in body.
     */
    @GetMapping("/expense-categories")
    public ResponseEntity<List<ExpenseCategorie>> getAllExpenseCategories(Pageable pageable) {
        log.debug("REST request to get a page of ExpenseCategories");
        Page<ExpenseCategorie> page = expenseCategorieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-categories/:id} : get the "id" expenseCategorie.
     *
     * @param id the id of the expenseCategorie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseCategorie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-categories/{id}")
    public ResponseEntity<ExpenseCategorie> getExpenseCategorie(@PathVariable Long id) {
        log.debug("REST request to get ExpenseCategorie : {}", id);
        Optional<ExpenseCategorie> expenseCategorie = expenseCategorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseCategorie);
    }

    /**
     * {@code DELETE  /expense-categories/:id} : delete the "id" expenseCategorie.
     *
     * @param id the id of the expenseCategorie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-categories/{id}")
    public ResponseEntity<Void> deleteExpenseCategorie(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseCategorie : {}", id);
        expenseCategorieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
