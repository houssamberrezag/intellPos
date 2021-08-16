package com.intell.pos.web.rest;

import com.intell.pos.domain.Sell;
import com.intell.pos.repository.SellRepository;
import com.intell.pos.service.SellService;
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
 * REST controller for managing {@link com.intell.pos.domain.Sell}.
 */
@RestController
@RequestMapping("/api")
public class SellResource {

    private final Logger log = LoggerFactory.getLogger(SellResource.class);

    private static final String ENTITY_NAME = "sell";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SellService sellService;

    private final SellRepository sellRepository;

    public SellResource(SellService sellService, SellRepository sellRepository) {
        this.sellService = sellService;
        this.sellRepository = sellRepository;
    }

    /**
     * {@code POST  /sells} : Create a new sell.
     *
     * @param sell the sell to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sell, or with status {@code 400 (Bad Request)} if the sell has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sells")
    public ResponseEntity<Sell> createSell(@Valid @RequestBody Sell sell) throws URISyntaxException {
        log.debug("REST request to save Sell : {}", sell);
        if (sell.getId() != null) {
            throw new BadRequestAlertException("A new sell cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sell result = sellService.save(sell);
        return ResponseEntity
            .created(new URI("/api/sells/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sells/:id} : Updates an existing sell.
     *
     * @param id the id of the sell to save.
     * @param sell the sell to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sell,
     * or with status {@code 400 (Bad Request)} if the sell is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sell couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sells/{id}")
    public ResponseEntity<Sell> updateSell(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Sell sell)
        throws URISyntaxException {
        log.debug("REST request to update Sell : {}, {}", id, sell);
        if (sell.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sell.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sell result = sellService.save(sell);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sell.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sells/:id} : Partial updates given fields of an existing sell, field will ignore if it is null
     *
     * @param id the id of the sell to save.
     * @param sell the sell to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sell,
     * or with status {@code 400 (Bad Request)} if the sell is not valid,
     * or with status {@code 404 (Not Found)} if the sell is not found,
     * or with status {@code 500 (Internal Server Error)} if the sell couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sells/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Sell> partialUpdateSell(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sell sell
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sell partially : {}, {}", id, sell);
        if (sell.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sell.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sell> result = sellService.partialUpdate(sell);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sell.getId().toString())
        );
    }

    /**
     * {@code GET  /sells} : get all the sells.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sells in body.
     */
    @GetMapping("/sells")
    public ResponseEntity<List<Sell>> getAllSells(Pageable pageable) {
        log.debug("REST request to get a page of Sells");
        Page<Sell> page = sellService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sells/:id} : get the "id" sell.
     *
     * @param id the id of the sell to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sell, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sells/{id}")
    public ResponseEntity<Sell> getSell(@PathVariable Long id) {
        log.debug("REST request to get Sell : {}", id);
        Optional<Sell> sell = sellService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sell);
    }

    /**
     * {@code DELETE  /sells/:id} : delete the "id" sell.
     *
     * @param id the id of the sell to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sells/{id}")
    public ResponseEntity<Void> deleteSell(@PathVariable Long id) {
        log.debug("REST request to delete Sell : {}", id);
        sellService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
