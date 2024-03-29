/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.web.rest;

import com.codahale.metrics.annotation.Timed;
import it.cnr.si.domain.Multa;
import it.cnr.si.domain.Veicolo;
import it.cnr.si.repository.VeicoloRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.repository.MultaRepository;
import it.cnr.si.repository.VeicoloRepository;
import it.cnr.si.service.MailService;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import it.cnr.si.web.rest.util.HeaderUtil;
import it.cnr.si.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Multa.
 */
@RestController
@RequestMapping("/api")
public class MultaResource {

    private final Logger log = LoggerFactory.getLogger(MultaResource.class);

    @Autowired
    private VeicoloRepository veicoloRepository;

    private static final String ENTITY_NAME = "multa";

    private final MultaRepository multaRepository;

    private final MailService mailService;

    private String TARGA;

    public MultaResource(MultaRepository multaRepository, MailService mailService) {
        this.multaRepository = multaRepository;
        this.mailService = mailService;
    }

    /**
     * POST  /multas : Create a new multa.
     *
     * @param multa the multa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new multa, or with status 400 (Bad Request) if the multa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/multas")
    @Timed
    public ResponseEntity<Multa> createMulta(@Valid @RequestBody Multa multa) throws URISyntaxException {
        log.debug("REST request to save Multa : {}", multa);
        if (multa.getId() != null) {
            throw new BadRequestAlertException("A new multa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        log.debug("dataMulta uguale: {}",multa.getDataMulta());

        ZonedDateTime dataVis = null;
        multa.setVisionatoMulta(dataVis);

        String data = multa.getDataMulta().toString().substring(0,10);
        String testo = "Controllare procedura Parco Auto CNR che è stata inserita una nuova multa da pagare per la vettura ("+multa.getVeicolo().getTarga()+") in data:"+data+". \n \n Procedura Parco Auto CNR";
        String mail = mailService.getEMailSedeDiAppartenenza(multa.getVeicolo().getResponsabile());
        log.debug("Multa mail a chi va: {}", mail);
        //da cancellare poi
        //mail = "valerio.diego@cnr.it";

        //TODO: inserire email parcoauto
        mailService.sendEmail(mail,"inserita multa da pagare in procedura",testo,false,false);
        //Fine mandare email
        Multa result = multaRepository.save(multa);
        return ResponseEntity.created(new URI("/api/multas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multas : Updates an existing multa.
     *
     * @param multa the multa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated multa,
     * or with status 400 (Bad Request) if the multa is not valid,
     * or with status 500 (Internal Server Error) if the multa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/multas")
    @Timed
    public ResponseEntity<Multa> updateMulta(@Valid @RequestBody Multa multa) throws URISyntaxException {
        log.debug("REST request to update Multa : {}", multa);
        if (multa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(multa.getVisionatoMulta() == null){
            multa.setVisionatoMulta(ZonedDateTime.now());
        }
        Multa result = multaRepository.save(multa);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, multa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multas : get all the multas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of multas in body
     */
    @GetMapping("/multas")
    @Timed
    public ResponseEntity<List<Multa>> getAllMultas(Pageable pageable) {
        log.debug("REST request to get a page of Multas");
        //Page<Multa> page = multaRepository.findAll(pageable);
        List<String> cdsuo = SecurityUtils.getCdSUO();
        Page<Multa> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = multaRepository.findByDeleted(false, pageable);
        } else {
            page = multaRepository.findByIstitutoStartsWithAndDeleted(cdsuo, false, pageable);
        }
        TARGA = "";
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/multas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /multas/:id : get the "id" multa.
     *
     * @param id the id of the multa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the multa, or with status 404 (Not Found)
     */
    @GetMapping("/multas/{id}")
    @Timed
    public ResponseEntity<Multa> getMulta(@PathVariable Long id) {
        log.debug("REST request to get Multa : {}", id);
        Optional<Multa> multa = multaRepository.findById(id);
        TARGA = multaRepository.findById(id).get().getVeicolo().getTarga();
        return ResponseUtil.wrapOrNotFound(multa);
    }

    /**
     * DELETE  /multas/:id : delete the "id" multa.
     *
     * @param id the id of the multa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/multas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMulta(@PathVariable Long id) {
        log.debug("REST request to delete Multa : {}", id);

        multaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    //Per richiamare Veicoli
    @GetMapping("/multas/findVeicolo")
    @Timed
    public ResponseEntity<List<Veicolo>> findVeicolo() {
        List<String> cdSUO = SecurityUtils.getCdSUO();
        List<Veicolo> veicoli;
        List<Veicolo> veicoliRimasti;

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            veicoli = veicoloRepository.findByDeletedFalse();
            veicoliRimasti = veicoloRepository.findByDeletedFalse();
        } else {
            veicoli = veicoloRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false);
            veicoliRimasti = veicoloRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false);
        }
        if(TARGA == null){

        }
        else if (TARGA.equals("")){
            TARGA = null;
        }
        if (TARGA != null) {
            Iterator i = veicoli.iterator();
            while (i.hasNext()) {
                Object v = i.next();
                if (((Veicolo) v).getTarga().equals(TARGA)) {
                } else {
                    veicoliRimasti.remove(v);
                }
            }
        }
        return ResponseEntity.ok(veicoliRimasti);
    }

}
