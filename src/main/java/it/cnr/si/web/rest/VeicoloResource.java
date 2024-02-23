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
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import io.github.jhipster.web.util.ResponseUtil;
import it.cnr.ict.service.SiglaService;
import it.cnr.si.domain.*;
import it.cnr.si.repository.*;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.AceService;
import it.cnr.si.service.CacheService;
import it.cnr.si.service.dto.PrintRequestBody;
import it.cnr.si.service.dto.VeicoloDetailPrintDto;
import it.cnr.si.service.dto.VeicoloPrintDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.scritture.UtenteDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleUtenteWebDto;
import it.cnr.si.web.rest.dto.VeicoloDTO;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import it.cnr.si.web.rest.util.HeaderUtil;
import it.cnr.si.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * REST controller for managing Veicolo.
 */
@RestController
@RequestMapping("/api")
public class VeicoloResource {

    private static final String ENTITY_NAME = "veicolo";
    private final AceService ace;
    private final CacheService cacheService;
    private final Logger log = LoggerFactory.getLogger(VeicoloResource.class);
    private final VeicoloRepository veicoloRepository;
    @Autowired(required = false)
    private SiglaService siglaService;
    private final VeicoloProprietaRepository veicoloProprietaRepository;
    private final VeicoloNoleggioRepository veicoloNoleggioRepository;
    private final BolloRepository bolloRepository;
    private final AssicurazioneVeicoloRepository assicurazioneVeicoloRepository;
    private final Print print;
    private ValidazioneResource validazioneResource;

    public VeicoloResource(VeicoloRepository veicoloRepository,@Autowired(required = false) AceService ace, CacheService cacheService,
                           VeicoloProprietaRepository veicoloProprietaRepository, VeicoloNoleggioRepository veicoloNoleggioRepository,
                           BolloRepository bolloRepository, AssicurazioneVeicoloRepository assicurazioneVeicoloRepository, @Autowired(required = false) Print print,
                           ValidazioneResource validazioneResource) {
        this.veicoloRepository = veicoloRepository;
        this.ace = ace;
        this.cacheService = cacheService;
        this.veicoloProprietaRepository = veicoloProprietaRepository;
        this.veicoloNoleggioRepository = veicoloNoleggioRepository;
        this.bolloRepository = bolloRepository;
        this.assicurazioneVeicoloRepository = assicurazioneVeicoloRepository;
        this.print = print;
        this.validazioneResource = validazioneResource;
    }

    /**
     * POST  /veicolos : Create a new veicolo.
     *
     * @param veicolo the veicolo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new veicolo, or with status 400 (Bad Request) if the veicolo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/veicolos")
    @Timed
    public ResponseEntity<Veicolo> createVeicolo(@Valid @RequestBody Veicolo veicolo) throws URISyntaxException {
        log.debug("REST request to save Veicolo : {}", veicolo);
        if (veicolo.getId() != null) {
            throw new BadRequestAlertException("A new veicolo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Veicolo result = veicoloRepository.save(veicolo);
        //funzione che crea validazione
       /** DA mettere solo quando è stato completato il procedimento del veicolo
        * String datiVeicolo;
        datiVeicolo = datiVeicolo(result);
        Validazione validazione = new Validazione();
        validazione.setTipologiaStato("Creazione");
        validazione.setDataModifica(LocalDate.now());
        validazione.setDescrizione("Inserita nuova auto:"+datiVeicolo);
        validazione.setVeicolo(result);
        validazioneResource.createValidazione(validazione);*/
        //Fine funzione
        return ResponseEntity.created(new URI("/api/veicolos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /veicolos : Updates an existing veicolo.
     *
     * @param veicolo the veicolo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated veicolo,
     * or with status 400 (Bad Request) if the veicolo is not valid,
     * or with status 500 (Internal Server Error) if the veicolo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/veicolos")
    @Timed
    public ResponseEntity<Veicolo> updateVeicolo(@Valid @RequestBody Veicolo veicolo) throws URISyntaxException {
        log.debug("REST request to update Veicolo : {}", veicolo);
        if (veicolo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(veicolo.getIstituto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Veicolo result = veicoloRepository.save(veicolo);
        //funzione che crea validazione
        String datiVeicolo;
        datiVeicolo = datiVeicolo(result);
        Validazione validazione = new Validazione();
        validazione.setTipologiaStato("Modifica");
        validazione.setDataModifica(Instant.now());
        validazione.setDescrizione("Modifica Veicolo:"+datiVeicolo);
        validazione.setVeicolo(result);
        validazioneResource.createValidazione(validazione);
        //Fine funzione
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, veicolo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /veicolos : get all the veicolos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of veicolos in body
     */
    @GetMapping("/veicolos")
    @Timed
    public ResponseEntity<List<Veicolo>> getAllVeicolos(Pageable pageable) {
        log.debug("REST request to get a page of Veicolos");

        List<String> cdSUO = SecurityUtils.getCdSUO();
        //allVeicoli();throws JSONException
        Page<Veicolo> veicoli;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN,AuthoritiesConstants.VIEWER)) {
            veicoli = veicoloRepository.findByDeletedFalse(pageable);
        } else {
            veicoli = veicoloRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(veicoli, "/api/veicolos");
        return new ResponseEntity<>(veicoli.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value="/veicolos/csv", produces = "text/csv")
    @Timed
    public ResponseEntity<List<VeicoloDTO>> getAllCSVVeicolos() {
        log.debug("REST request to get all Veicolos in csv");
        List<String> cdSUO = SecurityUtils.getCdSUO();
        List<Veicolo> veicoli;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN,AuthoritiesConstants.VIEWER)) {
            veicoli = veicoloRepository.findByDeletedFalse();
        } else {
            veicoli = veicoloRepository.findByIstitutoStartsWithAndDeleted(cdSUO, false);
        }
        return new ResponseEntity<>(veicoli.stream().map(veicolo -> new VeicoloDTO(veicolo)).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * GET  /veicolos/:id : get the "id" veicolo.
     *
     * @param id the id of the veicolo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the veicolo, or with status 404 (Not Found)
     */
    @GetMapping("/veicolos/{id}")
    @Timed
    public ResponseEntity<Veicolo> getVeicolo(@PathVariable Long id) {
        log.debug("REST request to get Veicolo : {}", id);

        Optional<Veicolo> veicolo = veicoloRepository.findById(id);
        if (!veicolo.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN, AuthoritiesConstants.VIEWER) ||
            cdSUO.contains(veicolo.get().getIstituto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseUtil.wrapOrNotFound(veicolo);
    }

    /**
     * DELETE  /veicolos/:id : delete the "id" veicolo.
     *
     * @param id the id of the veicolo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/veicolos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVeicolo(@PathVariable Long id) {
        log.debug("REST request to delete Veicolo : {}", id);
        Optional<Veicolo> veicolo = veicoloRepository.findById(id);
        if (!veicolo.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(veicolo.get().getIstituto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Veicolo vei = veicolo.get();
        vei.setDeleted(true);
        vei.setDeleted_note("USER = " + SecurityUtils.getCurrentUserLogin() + " DATA = " + LocalDateTime.now());
        veicoloRepository.save(vei);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //Per richiamare utenze ACE
    @GetMapping("/veicolos/findUtenza/{term}")
    @Timed
    public ResponseEntity<List<String>> findPersona(@PathVariable String term) {
        if (Optional.ofNullable(ace).isPresent()) {
            return ResponseEntity.ok(
                ace.searchUtenti(
                        Stream.of(
                            new AbstractMap.SimpleEntry<>("page", "0"),
                            new AbstractMap.SimpleEntry<>("offset", "20"),
                            new AbstractMap.SimpleEntry<>("username", term)
                        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    )
                    .stream()
                    .filter(utenteDto -> Optional.ofNullable(utenteDto.getUsername()).isPresent())
                    .map(SimpleUtenteWebDto::getUsername)
                    .collect(Collectors.toList()));
        } else {
            final List<String> users = Arrays.asList(CacheService.json2Java("showcase/utenti.json", String[].class));
            return ResponseEntity.ok(
                users
                    .stream()
                    .filter(s -> s.contains(term)).collect(Collectors.toList())
            );
        }
    }

    //Per richiamare istituti ACE
    @GetMapping("/veicolos/getIstituti")
    @Timed
    public ResponseEntity<List<SimpleEntitaOrganizzativaWebDto>> findIstituto() {

        List<String> cdSUO = SecurityUtils.getCdSUO();

        return ResponseEntity.ok(cacheService.getSediDiLavoro()
            .stream()
            .filter(entitaOrganizzativa -> Optional.ofNullable(entitaOrganizzativa.getCdsuo()).isPresent())
            .filter(entitaOrganizzativaWebDto -> {
                if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
                    return true;
                } else {
                    return cdSUO.contains(entitaOrganizzativaWebDto.getCdsuo());
                }
            })
            .sorted((i1, i2) -> i1.getCdsuo().compareTo(i2.getCdsuo()))
            .map(i -> {
                i.setDenominazione(i.getDenominazione().toUpperCase());
                return i;
            })
            .collect(Collectors.toList()));
    }

    @GetMapping("/veicolos/getAllVeicoli")
    @Timed
    @Transactional
    public ResponseEntity<Map<String, Object>> allVeicoli() throws IOException {
        List<String> cdSUO = SecurityUtils.getCdSUO();
        List<Veicolo> veicoliAll;
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN,AuthoritiesConstants.VIEWER))) {
            veicoliAll = veicoloRepository.findByIstitutoStartsWithAndDeleted(
                cdSUO,
                false);
        } else {
            veicoliAll = veicoloRepository.findAll(Sort.by("istituto"));
        }

        DateTimeFormatter formatter =
            DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .withLocale( Locale.ITALIAN )
                .withZone( ZoneId.systemDefault());
        VeicoloPrintDto veicoloPrintDto = new VeicoloPrintDto();
        veicoloPrintDto.setAnno(String.valueOf(LocalDate.now().getYear()));
        veicoloPrintDto.setVeicolos(
            veicoliAll.stream()
                .map(veicolo -> {
                    final Optional<VeicoloProprieta> veicoloProprietaOptional = veicoloProprietaRepository.findByVeicolo(veicolo);
                    final Stream<Bollo> bolloOptional = bolloRepository.findByVeicolo(veicolo);
                    final Stream<AssicurazioneVeicolo> assicurazioneVeicoloOptional = assicurazioneVeicoloRepository.findByVeicolo(veicolo);

                    final Optional<VeicoloNoleggio> veicoloNoleggioOptional = veicoloNoleggioRepository.findByVeicolo(veicolo);

                    return new VeicoloDetailPrintDto()
                        .setTarga(veicolo.getTarga())
                        .setIstituto(veicolo.getIstituto().concat(" - ").concat(veicolo.getCdsuo()))
                        .setResponsabile(veicolo.getResponsabile())
                        .setTipoProprieta(veicoloProprietaOptional.map(
                            veicoloProprieta -> {
                                return veicoloProprieta.getEtichetta() + " - " + veicoloProprieta.getRegioneImmatricolazione();
                            }
                        ).orElse(""))
                        .setBollo(bolloOptional.map(bollo -> {
                            return Optional.ofNullable(bollo.getDataScadenza()).map(instant -> formatter.format(instant)).orElse("");
                        }).collect(Collectors.joining(";")))
                        .setAssicurazione(assicurazioneVeicoloOptional.map(
                            assicurazioneVeicolo -> {
                                return "Compagnia:" + assicurazioneVeicolo.getCompagniaAssicurazione() + " Numero Polizza: " + assicurazioneVeicolo.getNumeroPolizza() +
                                    Optional.ofNullable(assicurazioneVeicolo.getDataScadenza()).map(instant -> " Scadenza:" + formatter.format(instant)).orElse("");
                            }
                        ).collect(Collectors.joining(";")))
                        .setNoleggio(veicoloNoleggioOptional.map(
                            veicoloNoleggio -> {
                                return "Società:" + veicoloNoleggio.getSocieta() + " Data Inizio:" + formatter.format(veicoloNoleggio.getDataInizioNoleggio()) +
                                    Optional.ofNullable(veicoloNoleggio.getDataFineNoleggio()).map(instant -> " Data Fine: " + formatter.format(instant)).orElse("");
                            }
                        ).orElse(""));
                }).collect(Collectors.toList())
        );
        ObjectMapper mapper = new ObjectMapper();
        final String jsonString = mapper.writeValueAsString(veicoloPrintDto);
        log.info(jsonString);
        final Response execute = print.execute(PrintRequestBody.create("/parcoauto/report_veicoli.jrxml", "Veicoli.pdf", jsonString));
        String encoded = Base64.getEncoder().encodeToString(StreamUtils.copyToByteArray(execute.body().asInputStream()));
        return new ResponseEntity<>(Collections.singletonMap("b64", encoded), HttpStatus.OK);
    }

    public String datiVeicolo(Veicolo veicolo) {
        String dati;
        //Veicolo
        dati = "Targa: " + veicolo.getTarga()
            + "Marca: " + veicolo.getMarca()
            + "Modello: " + veicolo.getModello()
            + "Cilindrata: " + veicolo.getCilindrata()
            + "CV KW: " + veicolo.getCvKw()
            + "Km Percorsi: " + veicolo.getKmPercorsi()
            + "Istituto: " + veicolo.getIstituto()
            + "CDSUO: " + veicolo.getCdsuo()
            + "Responsabile: " + veicolo.getResponsabile()
            + "Tipologia Veicolo: " + veicolo.getTipologiaVeicolo().getNome()
            + "Alimentazione Veicolo: " + veicolo.getAlimentazioneVeicolo().getNome()
            + "Classe Emissione Veicolo: " + veicolo.getClasseEmissioniVeicolo().getNome()
            + "Utilizzo Bene Veicolo: " + veicolo.getUtilizzoBeneVeicolo().getNome();
        return dati;
    }
}
