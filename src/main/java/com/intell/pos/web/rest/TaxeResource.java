package com.intell.pos.web.rest;

import com.intell.pos.domain.Taxe;
import com.intell.pos.repository.TaxeRepository;
import com.intell.pos.service.TaxeService;
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
 * REST controller for managing {@link com.intell.pos.domain.Taxe}.
 */
@RestController
@RequestMapping("/api")
public class TaxeResource {

    private final Logger log = LoggerFactory.getLogger(TaxeResource.class);

    private static final String ENTITY_NAME = "taxe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxeService taxeService;

    private final TaxeRepository taxeRepository;

    public TaxeResource(TaxeService taxeService, TaxeRepository taxeRepository) {
        this.taxeService = taxeService;
        this.taxeRepository = taxeRepository;
    }

    /**
     * {@code POST  /taxes} : Create a new taxe.
     *
     * @param taxe the taxe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxe, or with status {@code 400 (Bad Request)} if the taxe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/taxes")
    public ResponseEntity<Taxe> createTaxe(@Valid @RequestBody Taxe taxe) throws URISyntaxException {
        log.debug("REST request to save Taxe : {}", taxe);
        if (taxe.getId() != null) {
            throw new BadRequestAlertException("A new taxe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Taxe result = taxeService.save(taxe);
        return ResponseEntity
            .created(new URI("/api/taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /taxes/:id} : Updates an existing taxe.
     *
     * @param id the id of the taxe to save.
     * @param taxe the taxe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxe,
     * or with status {@code 400 (Bad Request)} if the taxe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/taxes/{id}")
    public ResponseEntity<Taxe> updateTaxe(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Taxe taxe)
        throws URISyntaxException {
        log.debug("REST request to update Taxe : {}, {}", id, taxe);
        if (taxe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Taxe result = taxeService.save(taxe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /taxes/:id} : Partial updates given fields of an existing taxe, field will ignore if it is null
     *
     * @param id the id of the taxe to save.
     * @param taxe the taxe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxe,
     * or with status {@code 400 (Bad Request)} if the taxe is not valid,
     * or with status {@code 404 (Not Found)} if the taxe is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/taxes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Taxe> partialUpdateTaxe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Taxe taxe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Taxe partially : {}, {}", id, taxe);
        if (taxe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Taxe> result = taxeService.partialUpdate(taxe);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxe.getId().toString())
        );
    }

    /**
     * {@code GET  /taxes} : get all the taxes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxes in body.
     */
    @GetMapping("/taxes")
    public ResponseEntity<List<Taxe>> getAllTaxes(Pageable pageable) {
        log.debug("REST request to get a page of Taxes");
        Page<Taxe> page = taxeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /taxes/:id} : get the "id" taxe.
     *
     * @param id the id of the taxe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/taxes/{id}")
    public ResponseEntity<Taxe> getTaxe(@PathVariable Long id) {
        log.debug("REST request to get Taxe : {}", id);
        Optional<Taxe> taxe = taxeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxe);
    }

    /**
     * {@code DELETE  /taxes/:id} : delete the "id" taxe.
     *
     * @param id the id of the taxe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/taxes/{id}")
    public ResponseEntity<Void> deleteTaxe(@PathVariable Long id) {
        log.debug("REST request to delete Taxe : {}", id);
        taxeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
