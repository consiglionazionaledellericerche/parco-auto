package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import si.cnr.it.domain.AssicurazioneVeicolo;
import si.cnr.it.repository.AssicurazioneVeicoloRepository;
import si.cnr.it.web.rest.errors.BadRequestAlertException;
import si.cnr.it.web.rest.util.HeaderUtil;
import si.cnr.it.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AssicurazioneVeicolo.
 */
@RestController
@RequestMapping("/api")
public class AssicurazioneVeicoloResource {

    private final Logger log = LoggerFactory.getLogger(AssicurazioneVeicoloResource.class);

    private static final String ENTITY_NAME = "assicurazioneVeicolo";

    private final AssicurazioneVeicoloRepository assicurazioneVeicoloRepository;

    public AssicurazioneVeicoloResource(AssicurazioneVeicoloRepository assicurazioneVeicoloRepository) {
        this.assicurazioneVeicoloRepository = assicurazioneVeicoloRepository;
    }

    /**
     * POST  /assicurazione-veicolos : Create a new assicurazioneVeicolo.
     *
     * @param assicurazioneVeicolo the assicurazioneVeicolo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assicurazioneVeicolo, or with status 400 (Bad Request) if the assicurazioneVeicolo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assicurazione-veicolos")
    @Timed
    public ResponseEntity<AssicurazioneVeicolo> createAssicurazioneVeicolo(@Valid @RequestBody AssicurazioneVeicolo assicurazioneVeicolo) throws URISyntaxException {
        log.debug("REST request to save AssicurazioneVeicolo : {}", assicurazioneVeicolo);
        if (assicurazioneVeicolo.getId() != null) {
            throw new BadRequestAlertException("A new assicurazioneVeicolo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssicurazioneVeicolo result = assicurazioneVeicoloRepository.save(assicurazioneVeicolo);
        return ResponseEntity.created(new URI("/api/assicurazione-veicolos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assicurazione-veicolos : Updates an existing assicurazioneVeicolo.
     *
     * @param assicurazioneVeicolo the assicurazioneVeicolo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assicurazioneVeicolo,
     * or with status 400 (Bad Request) if the assicurazioneVeicolo is not valid,
     * or with status 500 (Internal Server Error) if the assicurazioneVeicolo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assicurazione-veicolos")
    @Timed
    public ResponseEntity<AssicurazioneVeicolo> updateAssicurazioneVeicolo(@Valid @RequestBody AssicurazioneVeicolo assicurazioneVeicolo) throws URISyntaxException {
        log.debug("REST request to update AssicurazioneVeicolo : {}", assicurazioneVeicolo);
        if (assicurazioneVeicolo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AssicurazioneVeicolo result = assicurazioneVeicoloRepository.save(assicurazioneVeicolo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assicurazioneVeicolo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assicurazione-veicolos : get all the assicurazioneVeicolos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assicurazioneVeicolos in body
     */
    @GetMapping("/assicurazione-veicolos")
    @Timed
    public ResponseEntity<List<AssicurazioneVeicolo>> getAllAssicurazioneVeicolos(Pageable pageable) {
        log.debug("REST request to get a page of AssicurazioneVeicolos");
        Page<AssicurazioneVeicolo> page = assicurazioneVeicoloRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assicurazione-veicolos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assicurazione-veicolos/:id : get the "id" assicurazioneVeicolo.
     *
     * @param id the id of the assicurazioneVeicolo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assicurazioneVeicolo, or with status 404 (Not Found)
     */
    @GetMapping("/assicurazione-veicolos/{id}")
    @Timed
    public ResponseEntity<AssicurazioneVeicolo> getAssicurazioneVeicolo(@PathVariable Long id) {
        log.debug("REST request to get AssicurazioneVeicolo : {}", id);
        Optional<AssicurazioneVeicolo> assicurazioneVeicolo = assicurazioneVeicoloRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(assicurazioneVeicolo);
    }

    /**
     * DELETE  /assicurazione-veicolos/:id : delete the "id" assicurazioneVeicolo.
     *
     * @param id the id of the assicurazioneVeicolo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assicurazione-veicolos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssicurazioneVeicolo(@PathVariable Long id) {
        log.debug("REST request to delete AssicurazioneVeicolo : {}", id);

        assicurazioneVeicoloRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}