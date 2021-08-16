package com.intell.pos.web.rest;

import com.intell.pos.domain.CashRegister;
import com.intell.pos.repository.CashRegisterRepository;
import com.intell.pos.service.CashRegisterService;
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
 * REST controller for managing {@link com.intell.pos.domain.CashRegister}.
 */
@RestController
@RequestMapping("/api")
public class CashRegisterResource {

    private final Logger log = LoggerFactory.getLogger(CashRegisterResource.class);

    private static final String ENTITY_NAME = "cashRegister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashRegisterService cashRegisterService;

    private final CashRegisterRepository cashRegisterRepository;

    public CashRegisterResource(CashRegisterService cashRegisterService, CashRegisterRepository cashRegisterRepository) {
        this.cashRegisterService = cashRegisterService;
        this.cashRegisterRepository = cashRegisterRepository;
    }

    /**
     * {@code POST  /cash-registers} : Create a new cashRegister.
     *
     * @param cashRegister the cashRegister to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashRegister, or with status {@code 400 (Bad Request)} if the cashRegister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-registers")
    public ResponseEntity<CashRegister> createCashRegister(@Valid @RequestBody CashRegister cashRegister) throws URISyntaxException {
        log.debug("REST request to save CashRegister : {}", cashRegister);
        if (cashRegister.getId() != null) {
            throw new BadRequestAlertException("A new cashRegister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashRegister result = cashRegisterService.save(cashRegister);
        return ResponseEntity
            .created(new URI("/api/cash-registers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-registers/:id} : Updates an existing cashRegister.
     *
     * @param id the id of the cashRegister to save.
     * @param cashRegister the cashRegister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashRegister,
     * or with status {@code 400 (Bad Request)} if the cashRegister is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashRegister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-registers/{id}")
    public ResponseEntity<CashRegister> updateCashRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CashRegister cashRegister
    ) throws URISyntaxException {
        log.debug("REST request to update CashRegister : {}, {}", id, cashRegister);
        if (cashRegister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashRegister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CashRegister result = cashRegisterService.save(cashRegister);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashRegister.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cash-registers/:id} : Partial updates given fields of an existing cashRegister, field will ignore if it is null
     *
     * @param id the id of the cashRegister to save.
     * @param cashRegister the cashRegister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashRegister,
     * or with status {@code 400 (Bad Request)} if the cashRegister is not valid,
     * or with status {@code 404 (Not Found)} if the cashRegister is not found,
     * or with status {@code 500 (Internal Server Error)} if the cashRegister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cash-registers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CashRegister> partialUpdateCashRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CashRegister cashRegister
    ) throws URISyntaxException {
        log.debug("REST request to partial update CashRegister partially : {}, {}", id, cashRegister);
        if (cashRegister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashRegister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CashRegister> result = cashRegisterService.partialUpdate(cashRegister);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashRegister.getId().toString())
        );
    }

    /**
     * {@code GET  /cash-registers} : get all the cashRegisters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashRegisters in body.
     */
    @GetMapping("/cash-registers")
    public ResponseEntity<List<CashRegister>> getAllCashRegisters(Pageable pageable) {
        log.debug("REST request to get a page of CashRegisters");
        Page<CashRegister> page = cashRegisterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cash-registers/:id} : get the "id" cashRegister.
     *
     * @param id the id of the cashRegister to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashRegister, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-registers/{id}")
    public ResponseEntity<CashRegister> getCashRegister(@PathVariable Long id) {
        log.debug("REST request to get CashRegister : {}", id);
        Optional<CashRegister> cashRegister = cashRegisterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashRegister);
    }

    /**
     * {@code DELETE  /cash-registers/:id} : delete the "id" cashRegister.
     *
     * @param id the id of the cashRegister to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-registers/{id}")
    public ResponseEntity<Void> deleteCashRegister(@PathVariable Long id) {
        log.debug("REST request to delete CashRegister : {}", id);
        cashRegisterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
