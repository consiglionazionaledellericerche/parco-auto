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
import io.github.jhipster.web.util.ResponseUtil;
import it.cnr.ict.service.SiglaService;
import it.cnr.ict.service.dto.Contract;
import it.cnr.si.domain.Validazione;
import it.cnr.si.domain.Veicolo;
import it.cnr.si.domain.VeicoloNoleggio;
import it.cnr.si.domain.VeicoloProprieta;
import it.cnr.si.repository.VeicoloNoleggioRepository;
import it.cnr.si.repository.VeicoloProprietaRepository;
import it.cnr.si.repository.VeicoloRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import it.cnr.si.web.rest.util.HeaderUtil;
import it.cnr.si.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing VeicoloNoleggio.
 */
@RestController
@RequestMapping("/api")
public class VeicoloNoleggioResource {

    private static final String ENTITY_NAME = "veicoloNoleggio";
    private final Logger log = LoggerFactory.getLogger(VeicoloNoleggioResource.class);

    @Autowired(required = false)
    private SiglaService siglaService;
    @Autowired
    private VeicoloRepository veicoloRepository;
    @Autowired
    private VeicoloProprietaRepository veicoloProprietaRepository;
    @Autowired
    private VeicoloNoleggioRepository veicoloNoleggioRepository;
    private String TARGA;
    private ValidazioneResource validazioneResource;
    public VeicoloNoleggioResource(ValidazioneResource validazioneResource){
        this.validazioneResource = validazioneResource;
    }
    /**
     * POST  /veicolo-noleggios : Create a new veicoloNoleggio.
     *
     * @param veicoloNoleggio the veicoloNoleggio to create
     * @return the ResponseEntity with status 201 (Created) and with body the new veicoloNoleggio, or with status 400 (Bad Request) if the veicoloNoleggio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/veicolo-noleggios")
    @Timed
    public ResponseEntity<VeicoloNoleggio> createVeicoloNoleggio(@Valid @RequestBody VeicoloNoleggio veicoloNoleggio) throws URISyntaxException {
        log.debug("REST request to save VeicoloNoleggio : {}", veicoloNoleggio);
        if (veicoloNoleggio.getId() != null) {
            throw new BadRequestAlertException("A new veicoloNoleggio cannot already have an ID", ENTITY_NAME, "idexists");
        }

        //prende codiceTerzoDaSigla
        Optional<String> optCodiceTerzo = siglaService.getThirdPersonIdByPIVA(veicoloNoleggio.getPartitaIva());
        veicoloNoleggio.setCodiceTerzo(optCodiceTerzo.orElse(""));

        //prende repertorioContrattiNumero e Anno
        String siglaUo;
        String codiceTerzo;
        Optional<List<Contract>> repContratti;
        codiceTerzo = optCodiceTerzo.orElse("");
        if(!codiceTerzo.equals("")){
            siglaUo = veicoloNoleggio.getVeicolo().getIstituto()+"."+veicoloNoleggio.getVeicolo().getIstituto().substring(3);
            repContratti = siglaService.getContract(codiceTerzo,siglaUo);
            //repContratti = siglaService.getContract("63470","123.005");
            log.debug("siglaUo = {}",siglaUo);
            log.debug("repcontratti = {}",repContratti);
            if(repContratti.get().isEmpty() == false) {
                veicoloNoleggio.setRepContrattiNumero(repContratti.get().get(0).getPgContratto());
                veicoloNoleggio.setRepContrattiAnno(repContratti.get().get(0).getEsercizio());
            }
        }

        VeicoloNoleggio result = veicoloNoleggioRepository.save(veicoloNoleggio);

        //Inserisce validazione Direttore
        log.debug("Inserisce validazione Direttore");
        String datiVeicoloCompleto;
        datiVeicoloCompleto = datiVeicoloCompletoNoleggio(result);
        Validazione validazione = new Validazione();
        validazione.setVeicolo(result.getVeicolo());
        validazione.setDescrizione("Inserito nuovo veicolo a Nolegggio:"+datiVeicoloCompleto);
        validazione.setTipologiaStato("Inserito");
        validazione.setDataModifica(Instant.now());
        validazioneResource.createValidazione(validazione);
        log.debug("validazione {}",validazione);

        return ResponseEntity.created(new URI("/api/veicolo-noleggios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /veicolo-noleggios : Updates an existing veicoloNoleggio.
     *
     * @param veicoloNoleggio the veicoloNoleggio to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated veicoloNoleggio,
     * or with status 400 (Bad Request) if the veicoloNoleggio is not valid,
     * or with status 500 (Internal Server Error) if the veicoloNoleggio couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/veicolo-noleggios")
    @Timed
    public ResponseEntity<VeicoloNoleggio> updateVeicoloNoleggio(@Valid @RequestBody VeicoloNoleggio veicoloNoleggio) throws URISyntaxException {
        log.debug("REST request to update VeicoloNoleggio : {}", veicoloNoleggio);
        if (veicoloNoleggio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        List<String> cdSUO = SecurityUtils.getCdSUO();

        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(veicoloNoleggio.getVeicolo().getIstituto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //prende codiceTerzoDaSigla
        Optional<String> optCodiceTerzo = siglaService.getThirdPersonIdByPIVA(veicoloNoleggio.getPartitaIva());
        veicoloNoleggio.setCodiceTerzo(optCodiceTerzo.orElse(""));

        //prende repertorioContrattiNumero e Anno
        String siglaUo;
        String codiceTerzo;
        Optional<List<Contract>> repContratti;
        codiceTerzo = optCodiceTerzo.orElse("");
        if(!codiceTerzo.equals("")){
            siglaUo = veicoloNoleggio.getVeicolo().getIstituto()+"."+veicoloNoleggio.getVeicolo().getIstituto().substring(3);
            repContratti = siglaService.getContract(codiceTerzo,siglaUo);
            //repContratti = siglaService.getContract("63470","123.005");
            log.debug("siglaUo = {}",siglaUo);
            log.debug("repcontratti = {}",repContratti);
            if(repContratti.get().isEmpty() == false) {
                veicoloNoleggio.setRepContrattiNumero(repContratti.get().get(0).getPgContratto());
                veicoloNoleggio.setRepContrattiAnno(repContratti.get().get(0).getEsercizio());
            }
        }

        VeicoloNoleggio result = veicoloNoleggioRepository.save(veicoloNoleggio);

        //Inserisce validazione Direttore
        log.debug("Inserisce validazione Direttore");
        String datiVeicoloCompleto;
        datiVeicoloCompleto = datiVeicoloCompletoNoleggio(result);
        Validazione validazione = new Validazione();
        validazione.setVeicolo(result.getVeicolo());
        validazione.setDescrizione("Modifica effettuata in veicolo a Noleggio:"+datiVeicoloCompleto);
        validazione.setTipologiaStato("Modifica");
        validazione.setDataModifica(Instant.now());
        validazioneResource.createValidazione(validazione);
        log.debug("validazione {}",validazione);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, veicoloNoleggio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /veicolo-noleggios : get all the veicoloNoleggios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of veicoloNoleggios in body
     */
    @GetMapping("/veicolo-noleggios")
    @Timed
    public ResponseEntity<List<VeicoloNoleggio>> getAllVeicoloNoleggios(Pageable pageable) {
        log.debug("REST request to get a page of VeicoloNoleggios");
        List<String> cdSUO = SecurityUtils.getCdSUO();

        Page<VeicoloNoleggio> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            page = veicoloNoleggioRepository.findAllActive(false, pageable);
        else
            page = veicoloNoleggioRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false, pageable);

        TARGA = "";
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/veicolo-noleggios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /veicolo-noleggios/:id : get the "id" veicoloNoleggio.
     *
     * @param id the id of the veicoloNoleggio to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the veicoloNoleggio, or with status 404 (Not Found)
     */
    @GetMapping("/veicolo-noleggios/{id}")
    @Timed
    public ResponseEntity<VeicoloNoleggio> getVeicoloNoleggio(@PathVariable Long id) {
        log.debug("REST request to get VeicoloNoleggio : {}", id);
        Optional<VeicoloNoleggio> veicoloNoleggio = veicoloNoleggioRepository.findById(id);
        TARGA = veicoloNoleggioRepository.findById(id).get().getVeicolo().getTarga();
        return ResponseUtil.wrapOrNotFound(veicoloNoleggio);
    }

    /**
     * DELETE  /veicolo-noleggios/:id : delete the "id" veicoloNoleggio.
     *
     * @param id the id of the veicoloNoleggio to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/veicolo-noleggios/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<Void> deleteVeicoloNoleggio(@PathVariable Long id) {
        log.debug("REST request to delete VeicoloNoleggio : {}", id);

        veicoloNoleggioRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //Per richiamare Veicoli
    @GetMapping("/veicolo-noleggios/findVeicolo")
    @Timed
    public ResponseEntity<List<Veicolo>> findVeicolo() {

        List<Veicolo> veicoliRimasti;

        List<Veicolo> veicoli;

        List<VeicoloProprieta> allVeicoliProprieta;

        List<VeicoloNoleggio> allVeicoliNoleggio;

        List<String> cdSUO = SecurityUtils.getCdSUO();

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            veicoliRimasti = veicoloRepository.findByDeletedFalse();
            veicoli = veicoloRepository.findByDeletedFalse();
        } else {
            veicoliRimasti = veicoloRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false);
            veicoli = veicoloRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false);
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
        } else {
            allVeicoliProprieta = veicoloProprietaRepository.findAllActive(false);
            allVeicoliNoleggio = veicoloNoleggioRepository.findAllActive(false);
            Iterator i = veicoli.iterator();
            while (i.hasNext()) {
                Object v = i.next();
                Iterator iavp = allVeicoliProprieta.iterator();
                Iterator iavn = allVeicoliNoleggio.iterator();
                while (iavp.hasNext()) {
                    Object vp = iavp.next();
                    if (((VeicoloProprieta) vp).getVeicolo().getTarga().equals(((Veicolo) v).getTarga())) {
                        veicoliRimasti.remove(v);
                    }
                }
                while (iavn.hasNext()) {
                    Object vn = iavn.next();
                    if (((VeicoloNoleggio) vn).getVeicolo().getTarga().equals(((Veicolo) v).getTarga())) {
                        veicoliRimasti.remove(v);
                    }
                }

            }
        }


        return ResponseEntity.ok(veicoliRimasti);
    }

    public String datiVeicoloCompletoNoleggio(VeicoloNoleggio veicoloNoleggio){
        String dati;
        //Veicolo
        dati = "Targa: "+veicoloNoleggio.getVeicolo().getTarga()
            +"Marca: "+veicoloNoleggio.getVeicolo().getMarca()
            +"Modello: "+veicoloNoleggio.getVeicolo().getModello()
            +"Cilindrata: "+veicoloNoleggio.getVeicolo().getCilindrata()
            +"CV KW: "+veicoloNoleggio.getVeicolo().getCvKw()
            +"Km Percorsi: "+veicoloNoleggio.getVeicolo().getKmPercorsi()
            +"Istituto: "+veicoloNoleggio.getVeicolo().getIstituto()
            +"CDSUO: "+veicoloNoleggio.getVeicolo().getCdsuo()
            +"Responsabile: "+veicoloNoleggio.getVeicolo().getResponsabile()
            +"Tipologia Veicolo: "+veicoloNoleggio.getVeicolo().getTipologiaVeicolo().getNome()
            +"Alimentazione Veicolo: "+veicoloNoleggio.getVeicolo().getAlimentazioneVeicolo().getNome()
            +"Classe Emissione Veicolo: "+veicoloNoleggio.getVeicolo().getClasseEmissioniVeicolo().getNome()
            +"Utilizzo Bene Veicolo: "+veicoloNoleggio.getVeicolo().getUtilizzoBeneVeicolo().getNome();
        //VeicoloNoleggio
            dati = dati+"Società: "+veicoloNoleggio.getSocieta()
            +"Partita Iva: "+veicoloNoleggio.getPartitaIva()
            +"Data Inizio Noleggio: "+veicoloNoleggio.getDataInizioNoleggio()
            +"Data Fine Noleggio: "+veicoloNoleggio.getDataFineNoleggio();


            String dataCessAnticipata;
            String dataProroga;
            String repContrattiNumero;
            String repContrattiAnno;
            String codiceTerzo;

            if(veicoloNoleggio.getCodiceTerzo() == null){
                codiceTerzo = "";
            }
            else{
                codiceTerzo = veicoloNoleggio.getCodiceTerzo();
            }
            if(veicoloNoleggio.getDataCessazioneAnticipata() == null){
                dataCessAnticipata = "";
            }
            else{
                dataCessAnticipata = veicoloNoleggio.getDataCessazioneAnticipata().toString();
            }
        if(veicoloNoleggio.getDataProroga() == null){
            dataProroga = "";
        }
        else{
            dataProroga = veicoloNoleggio.getDataProroga().toString();
        }
        if(veicoloNoleggio.getRepContrattiNumero() == null){
            repContrattiNumero = "";
        }
        else{
            repContrattiNumero = veicoloNoleggio.getRepContrattiNumero().toString();
        }
        if(veicoloNoleggio.getRepContrattiAnno() == null){
            repContrattiAnno = "";
        }
        else{
            repContrattiAnno = veicoloNoleggio.getRepContrattiAnno().toString();
        }
            dati = dati+"Codice Terzo: "+codiceTerzo
                +"Data Cessazione Anticipata: "+ dataCessAnticipata
            +"Data Proroga: "+dataProroga
            +"Repertorio Contratti Anno: "+repContrattiNumero
            +"Repertorio Contratti Numero: "+repContrattiAnno;
        return dati;
    }
}
