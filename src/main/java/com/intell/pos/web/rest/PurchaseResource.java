package com.intell.pos.web.rest;

import com.intell.pos.domain.Payment;
import com.intell.pos.domain.Product;
import com.intell.pos.domain.Purchase;
import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.enumeration.PaymentTypes;
import com.intell.pos.domain.enumeration.TransactionTypes;
import com.intell.pos.repository.PurchaseRepository;
import com.intell.pos.service.PaymentService;
import com.intell.pos.service.ProductService;
import com.intell.pos.service.PurchaseService;
import com.intell.pos.service.TransactionService;
import com.intell.pos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * REST controller for managing {@link com.intell.pos.domain.Purchase}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    private static final String ENTITY_NAME = "purchase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseService purchaseService;

    private final PurchaseRepository purchaseRepository;

    private final TransactionService transactionService;

    private final ProductService productService;

    private final PaymentService paymentService;

    public PurchaseResource(
        PurchaseService purchaseService,
        PurchaseRepository purchaseRepository,
        TransactionService transactionService,
        ProductService productService,
        PaymentService paymentService
    ) {
        this.purchaseService = purchaseService;
        this.purchaseRepository = purchaseRepository;
        this.transactionService = transactionService;
        this.productService = productService;
        this.paymentService = paymentService;
    }

    /**
     * {@code POST  /purchases} : Create a new purchase.
     *
     * @param purchase the purchase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchase, or with status {@code 400 (Bad Request)} if the purchase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchases_")
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchase);
        if (purchase.getId() != null) {
            throw new BadRequestAlertException("A new purchase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Purchase result = purchaseService.save(purchase);
        return ResponseEntity
            .created(new URI("/api/purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /purchases} : Create a new purchase.
     *
     * @param purchase the purchase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchase, or with status {@code 400 (Bad Request)} if the purchase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchases")
    public ResponseEntity<Purchase> createPurchases(
        @Valid @RequestBody List<Purchase> purchases,
        @RequestParam double paid,
        @RequestParam double discountAmount,
        @RequestParam String paymentMethod
    ) throws URISyntaxException {
        log.debug("REST request to save Purchase  : {}", purchases);

        if (purchases.size() == 0) {
            throw new BadRequestAlertException("achat invalide", ENTITY_NAME, "Veuillez sélectionner un ou plusieurs produits");
        }

        if (paid < 0) {
            throw new BadRequestAlertException("Montant payé invalid", ENTITY_NAME, "Veuillez insérer un montant supérieur ou égale 0");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM");
        String ym = formatter.format(LocalDate.now());
        int row = transactionService.findByTransactiontype(TransactionTypes.PURCHASE).size() + 1;
        String ref_no = ym + "/P-" + row;
        double total = 0.0;
        for (Purchase purchase : purchases) {
            if (purchase.getPerson() == null || purchase.getPerson().getId() == null) {
                throw new BadRequestAlertException("fournisseur obligatoir", ENTITY_NAME, "Veuillez sélectionner un fournisseur");
            }

            if (purchase.getQuantity() <= 0) {
                throw new BadRequestAlertException(
                    "Quantité requise",
                    ENTITY_NAME,
                    "La quantité de produit" + purchase.getProduct().getName() + " est requise"
                );
            }
            if (purchase.getProduct() == null || purchase.getProduct().getId() == null) {
                throw new BadRequestAlertException("produit null", ENTITY_NAME, "produit est requise");
            }

            purchase.setSubTotal(purchase.getQuantity() * purchase.getUnitCost());

            total += purchase.getSubTotal();

            purchase.setId(null);
            purchase.setReferenceNo(ref_no);
            purchase.setCreatedAt(Instant.now());

            purchaseService.save(purchase);

            Product product = purchase.getProduct();
            product.setQuantity((product.getQuantity() != null ? product.getQuantity() : 0.0) + purchase.getQuantity());
            productService.save(product);
        }

        double total_payable = total - discountAmount;

        Transaction transaction = new Transaction();
        transaction.setReferenceNo(ref_no);
        transaction.setPerson(purchases.get(0).getPerson());
        transaction.setTransactionType(TransactionTypes.PURCHASE);
        transaction.setDiscount(discountAmount);
        transaction.setTotal(total);
        transaction.setNetTotal(total_payable);
        transaction.setDate(Instant.now());
        transaction.setPaid(paid);
        transaction.setPos(false);
        transaction.setLaborCost(0.0);
        transaction.setCreatedAt(Instant.now());
        transactionService.save(transaction);

        if (paid > 0) {
            Payment payment = new Payment();
            payment.setPerson(purchases.get(0).getPerson());
            payment.setAmount(paid);
            payment.setMethod(paymentMethod);
            payment.setType(PaymentTypes.DEBIT);
            payment.setReferenceNo(ref_no);
            payment.setNote("Paid for bill " + ref_no);
            payment.setDate(Instant.now());
            payment.setCreatedAt(Instant.now());

            paymentService.save(payment);
        }

        //Purchase result = purchaseService.save(purchase);
        return ResponseEntity.ok(purchases.get(0));
    }

    /**
     * {@code PUT  /purchases/:id} : Updates an existing purchase.
     *
     * @param id the id of the purchase to save.
     * @param purchase the purchase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchase,
     * or with status {@code 400 (Bad Request)} if the purchase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchases/{id}")
    public ResponseEntity<Purchase> updatePurchase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Purchase purchase
    ) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}, {}", id, purchase);
        if (purchase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Purchase result = purchaseService.save(purchase);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchase.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchases/:id} : Partial updates given fields of an existing purchase, field will ignore if it is null
     *
     * @param id the id of the purchase to save.
     * @param purchase the purchase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchase,
     * or with status {@code 400 (Bad Request)} if the purchase is not valid,
     * or with status {@code 404 (Not Found)} if the purchase is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchases/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Purchase> partialUpdatePurchase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Purchase purchase
    ) throws URISyntaxException {
        log.debug("REST request to partial update Purchase partially : {}, {}", id, purchase);
        if (purchase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Purchase> result = purchaseService.partialUpdate(purchase);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchase.getId().toString())
        );
    }

    /**
     * {@code GET  /purchases} : get all the purchases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchases in body.
     */
    @GetMapping("/purchases")
    public ResponseEntity<List<Purchase>> getAllPurchases(Pageable pageable) {
        log.debug("REST request to get a page of Purchases");
        Page<Purchase> page = purchaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchases/:id} : get the "id" purchase.
     *
     * @param id the id of the purchase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchases/{id}")
    public ResponseEntity<Purchase> getPurchase(@PathVariable Long id) {
        log.debug("REST request to get Purchase : {}", id);
        Optional<Purchase> purchase = purchaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchase);
    }

    /**
     * {@code DELETE  /purchases/:id} : delete the "id" purchase.
     *
     * @param id the id of the purchase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        log.debug("REST request to delete Purchase : {}", id);
        purchaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /purchases} : get purchases by reference.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchases in body.
     */
    @GetMapping("/purchasesByReference")
    public ResponseEntity<List<Purchase>> purchasesByReference(@RequestParam String reference) {
        log.debug("REST request to get list of Purchases by reference");
        List<Purchase> purchases = purchaseService.findByReference(reference);
        // HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok(purchases);
    }
}
