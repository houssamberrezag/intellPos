package com.intell.pos.web.rest;

import com.intell.pos.domain.Subcategorie;
import com.intell.pos.repository.SubcategorieRepository;
import com.intell.pos.service.SubcategorieService;
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
 * REST controller for managing {@link com.intell.pos.domain.Subcategorie}.
 */
@RestController
@RequestMapping("/api")
public class SubcategorieResource {

    private final Logger log = LoggerFactory.getLogger(SubcategorieResource.class);

    private static final String ENTITY_NAME = "subcategorie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubcategorieService subcategorieService;

    private final SubcategorieRepository subcategorieRepository;

    public SubcategorieResource(SubcategorieService subcategorieService, SubcategorieRepository subcategorieRepository) {
        this.subcategorieService = subcategorieService;
        this.subcategorieRepository = subcategorieRepository;
    }

    /**
     * {@code POST  /subcategories} : Create a new subcategorie.
     *
     * @param subcategorie the subcategorie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subcategorie, or with status {@code 400 (Bad Request)} if the subcategorie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subcategories")
    public ResponseEntity<Subcategorie> createSubcategorie(@Valid @RequestBody Subcategorie subcategorie) throws URISyntaxException {
        log.debug("REST request to save Subcategorie : {}", subcategorie);
        if (subcategorie.getId() != null) {
            throw new BadRequestAlertException("A new subcategorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Subcategorie result = subcategorieService.save(subcategorie);
        return ResponseEntity
            .created(new URI("/api/subcategories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subcategories/:id} : Updates an existing subcategorie.
     *
     * @param id the id of the subcategorie to save.
     * @param subcategorie the subcategorie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subcategorie,
     * or with status {@code 400 (Bad Request)} if the subcategorie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subcategorie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subcategories/{id}")
    public ResponseEntity<Subcategorie> updateSubcategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Subcategorie subcategorie
    ) throws URISyntaxException {
        log.debug("REST request to update Subcategorie : {}, {}", id, subcategorie);
        if (subcategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subcategorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subcategorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Subcategorie result = subcategorieService.save(subcategorie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subcategorie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subcategories/:id} : Partial updates given fields of an existing subcategorie, field will ignore if it is null
     *
     * @param id the id of the subcategorie to save.
     * @param subcategorie the subcategorie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subcategorie,
     * or with status {@code 400 (Bad Request)} if the subcategorie is not valid,
     * or with status {@code 404 (Not Found)} if the subcategorie is not found,
     * or with status {@code 500 (Internal Server Error)} if the subcategorie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subcategories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Subcategorie> partialUpdateSubcategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Subcategorie subcategorie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subcategorie partially : {}, {}", id, subcategorie);
        if (subcategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subcategorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subcategorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Subcategorie> result = subcategorieService.partialUpdate(subcategorie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subcategorie.getId().toString())
        );
    }

    /**
     * {@code GET  /subcategories} : get all the subcategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subcategories in body.
     */
    @GetMapping("/subcategories")
    public ResponseEntity<List<Subcategorie>> getAllSubcategories(Pageable pageable) {
        log.debug("REST request to get a page of Subcategories");
        Page<Subcategorie> page = subcategorieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subcategories/:id} : get the "id" subcategorie.
     *
     * @param id the id of the subcategorie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subcategorie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subcategories/{id}")
    public ResponseEntity<Subcategorie> getSubcategorie(@PathVariable Long id) {
        log.debug("REST request to get Subcategorie : {}", id);
        Optional<Subcategorie> subcategorie = subcategorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subcategorie);
    }

    /**
     * {@code DELETE  /subcategories/:id} : delete the "id" subcategorie.
     *
     * @param id the id of the subcategorie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<Void> deleteSubcategorie(@PathVariable Long id) {
        log.debug("REST request to delete Subcategorie : {}", id);
        subcategorieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
