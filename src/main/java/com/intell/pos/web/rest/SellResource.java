package com.intell.pos.web.rest;

import com.intell.pos.domain.Payment;
import com.intell.pos.domain.Product;
import com.intell.pos.domain.ReturnTransaction;
import com.intell.pos.domain.Sell;
import com.intell.pos.domain.Transaction;
import com.intell.pos.domain.User;
import com.intell.pos.domain.enumeration.PaymentTypes;
import com.intell.pos.domain.enumeration.TransactionTypes;
import com.intell.pos.repository.SellRepository;
import com.intell.pos.security.SecurityUtils;
import com.intell.pos.service.PaymentService;
import com.intell.pos.service.ProductService;
import com.intell.pos.service.ReturnTransactionService;
import com.intell.pos.service.SellService;
import com.intell.pos.service.TransactionService;
import com.intell.pos.service.UserService;
import com.intell.pos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final TransactionService transactionService;

    private final ProductService productService;

    private final PaymentService paymentService;

    private final ReturnTransactionService returnTransactionService;

    private final UserService userService;

    public SellResource(
        SellService sellService,
        SellRepository sellRepository,
        TransactionService transactionService,
        ProductService productService,
        PaymentService paymentService,
        ReturnTransactionService returnTransactionService,
        UserService userService
    ) {
        this.sellService = sellService;
        this.sellRepository = sellRepository;
        this.transactionService = transactionService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.returnTransactionService = returnTransactionService;
        this.userService = userService;
    }

    /**
     * {@code POST  /sells} : Create a new sell.
     *
     * @param sell the sell to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sell, or with status {@code 400 (Bad Request)} if the sell has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sells_")
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
     * {@code POST  /sells} : Create a new sell.
     *
     * @param sell the sell to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sell, or with status {@code 400 (Bad Request)} if the sell has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sells")
    public ResponseEntity<Sell> createSells(
        @Valid @RequestBody List<Sell> sells,
        @RequestParam double paid,
        @RequestParam double shippingCost,
        @RequestParam double discountAmount,
        @RequestParam String paymentMethod
    ) throws URISyntaxException {
        log.debug("REST request to save Sell  : {}", sells);

        if (sells.size() == 0) {
            throw new BadRequestAlertException("vente invalide", ENTITY_NAME, "Veuillez sélectionner un ou plusieurs produits");
        }

        if (paid < 0) {
            throw new BadRequestAlertException("Montant payé invalid", ENTITY_NAME, "Veuillez insérer un montant supérieur ou égale 0");
        }

        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM");
        String ym = formatter.format(LocalDate.now());*/
        int row = transactionService.findByTransactiontype(TransactionTypes.SELL).size() + 1;
        String ref_no = "INV-" + row;
        double total = 0.0;
        double total_cost_price = 0.0;
        for (Sell sell : sells) {
            if (sell.getPerson() == null || sell.getPerson().getId() == null) {
                throw new BadRequestAlertException("client obligatoir", ENTITY_NAME, "Veuillez sélectionner un client");
            }

            if (sell.getQuantity() <= 0) {
                throw new BadRequestAlertException(
                    "Quantité requise",
                    ENTITY_NAME,
                    "La quantité de produit" + sell.getProduct().getName() + " est requise"
                );
            }
            if (sell.getProduct() == null || sell.getProduct().getId() == null) {
                throw new BadRequestAlertException("produit null", ENTITY_NAME, "produit est requise");
            }

            sell.setUnitCostPrice(sell.getProduct().getCostPrice());

            sell.setSubTotal(sell.getQuantity() * sell.getUnitPrice()); // à vérifier -- vérifié

            total += sell.getSubTotal();
            total_cost_price += (sell.getUnitCostPrice() * sell.getQuantity());

            sell.setId(null);
            sell.setReferenceNo(ref_no);
            sell.setCreatedAt(Instant.now());

            sellService.save(sell);

            Product product = sell.getProduct();
            product.setQuantity((product.getQuantity() != null ? product.getQuantity() : 0.0) - sell.getQuantity());
            productService.save(product);
        }

        double total_payable = total - discountAmount;

        Transaction transaction = new Transaction();
        transaction.setReferenceNo(ref_no);
        transaction.setPerson(sells.get(0).getPerson());
        transaction.setTransactionType(TransactionTypes.SELL);
        transaction.setDiscount(discountAmount);
        transaction.setTotal(total);
        transaction.setTotalCostPrice(total_cost_price);
        transaction.setNetTotal(total_payable);
        transaction.setDate(Instant.now());
        transaction.setPaid(paid);
        transaction.setPos(false);
        transaction.setLaborCost(shippingCost);
        transaction.setCreatedAt(Instant.now());
        transactionService.save(transaction);

        if (paid > 0) {
            Payment payment = new Payment();
            payment.setPerson(sells.get(0).getPerson());
            payment.setAmount(paid);
            payment.setMethod(paymentMethod);
            payment.setType(PaymentTypes.CREDIT);
            payment.setReferenceNo(ref_no);
            payment.setNote("Paid for invoice " + ref_no);
            payment.setDate(Instant.now());
            payment.setCreatedAt(Instant.now());

            paymentService.save(payment);
        }

        //Purchase result = purchaseService.save(purchase);
        return ResponseEntity.ok(sells.get(0));
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

    @GetMapping("/sellsByReference")
    public ResponseEntity<List<Sell>> sellsByReference(@RequestParam String reference) {
        log.debug("REST request to get list of Sells by reference");
        List<Sell> sells = sellService.findByReference(reference);
        return ResponseEntity.ok(sells);
    }

    @PostMapping("/sells/return")
    public ResponseEntity<List<Sell>> returnSells(@RequestBody List<String> sellsDataString, @RequestParam Long transactionId)
        throws URISyntaxException {
        Transaction transaction = transactionService.findOne(transactionId).get();
        if (transaction == null) throw new BadRequestAlertException("Transaction not found", ENTITY_NAME, "idnotfound");

        Map<Long, Integer> sellsWithReturnQuantity = new HashMap<Long, Integer>();
        sellsDataString.forEach(
            s -> {
                String key = s.split(",")[0];
                Integer value = Integer.valueOf(s.split(",")[1]);
                if (value > 0) sellsWithReturnQuantity.put(Long.valueOf(key), value);
            }
        );
        sellsWithReturnQuantity.forEach(
            (sellId, quantity) -> {
                Sell sell = sellService.findOne(sellId).get();
                sell.setQuantity(sell.getQuantity() - quantity);
                sell.setSubTotal(sell.getQuantity() * sell.getUnitPrice());
                sellService.save(sell);

                Product product = sell.getProduct();
                product.setQuantity(product.getQuantity() + quantity);
                productService.save(product);

                String login = SecurityUtils.getCurrentUserLogin().orElse("anonymoususer");
                Optional<User> optionalUser = userService.getUserWithAuthoritiesByLogin(login);

                ReturnTransaction returnTransaction = new ReturnTransaction();
                returnTransaction.setSellsReferenceNo(sell.getReferenceNo());
                returnTransaction.setCreatedAt(Instant.now());
                returnTransaction.setSell(sell);
                returnTransaction.setPerson(transaction.getPerson());
                returnTransaction.setReturnAmount(quantity * sell.getUnitPrice());
                returnTransaction.setReturnUnits(quantity);
                returnTransaction.setReturnVat(0.0);
                if (optionalUser.isPresent()) returnTransaction.setReturnedBy(Integer.valueOf(optionalUser.get().getId().toString()));

                returnTransactionService.save(returnTransaction);
            }
        );

        List<Sell> sells = sellService.findByReference(transaction.getReferenceNo());
        Double total = sells.stream().mapToDouble(sell -> sell.getSubTotal()).sum();
        Double total_cost_price = sells.stream().mapToDouble(sell -> sell.getUnitCostPrice() * sell.getQuantity()).sum();
        Double net_total = total + transaction.getLaborCost() - transaction.getDiscount();
        transaction.setTotal(total);
        transaction.setTotalCostPrice(total_cost_price);
        transaction.setNetTotal(net_total);
        transactionService.save(transaction);

        return ResponseEntity.ok(sells);
    }
}
