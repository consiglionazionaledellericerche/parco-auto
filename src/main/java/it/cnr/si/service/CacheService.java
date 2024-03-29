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

package it.cnr.si.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.si.domain.*;
import it.cnr.si.repository.AssicurazioneVeicoloRepository;
import it.cnr.si.repository.BolloRepository;
import it.cnr.si.repository.VeicoloNoleggioRepository;
import it.cnr.si.repository.VeicoloProprietaRepository;
import it.cnr.si.service.dto.anagrafica.base.NodeDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import it.cnr.si.web.rest.AssicurazioneVeicoloResource;
import it.cnr.si.web.rest.BolloResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.File;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CacheService {

    public static final String ACE_GERARCHIA_ISTITUTI = "ACE-GERARCHIA-ISTITUTI";
    public static final String ACE_GERARCHIA_UFFICI = "ACE-GERARCHIA-UFFICI";
    public static final String ACE_SEDE_LAVORO = "ACE-SEDE-LAVORO";
    @Autowired
    CacheManager cacheManager;
    @Autowired(required = false)
    private AceService aceService;

    private BolloRepository bolloRepository;
    private AssicurazioneVeicoloRepository assicurazioneVeicoloRepository;
    private VeicoloProprietaRepository veicoloProprietaRepository;
    private VeicoloNoleggioRepository veicoloNoleggioRepository;
    private MailService mailService;

    public CacheService(VeicoloProprietaRepository veicoloProprietaRepository, VeicoloNoleggioRepository veicoloNoleggioRepository,
                        BolloRepository bolloRepository, AssicurazioneVeicoloRepository assicurazioneVeicoloRepository, MailService mailService){
        this.veicoloProprietaRepository = veicoloProprietaRepository;
        this.veicoloNoleggioRepository = veicoloNoleggioRepository;
        this.bolloRepository = bolloRepository;
        this.assicurazioneVeicoloRepository = assicurazioneVeicoloRepository;
        this.mailService = mailService;
    }

    @Cacheable(ACE_GERARCHIA_ISTITUTI)
    public List<NodeDto> getGerarchiaIstituti() {
        return aceService.getGerarchiaIstituti();
    }

    @Cacheable(ACE_GERARCHIA_UFFICI)
    public List<NodeDto> getGerarchiaUffici() {
        return aceService.getGerarchiaUffici();
    }
    @Cacheable(ACE_SEDE_LAVORO)
    public List<SimpleEntitaOrganizzativaWebDto> getSediDiLavoro() {
        if (aceService != null) {
            return aceService.entitaOrganizzativaFind(null, null, LocalDate.now(), null);
        } else {
            return Arrays.asList(json2Java("showcase/entita_organizzativa.json", SimpleEntitaOrganizzativaWebDto[].class));
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")
    public void evictAllcachesAtIntervals() {
        evictAllCaches();
    }

    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
            .filter(s -> s.equals(ACE_GERARCHIA_ISTITUTI) || s.equals(ACE_GERARCHIA_UFFICI) || s.equals(ACE_SEDE_LAVORO))
            .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void creaBolloAnno() {
        creaBolloEAssicurazione();
    }

    public void creaBolloEAssicurazione(){ //TODO: mettere a un mese di distanza l'inserimento del bollo e assicurzione
        LocalDate oggi = LocalDate.now();
        int ggOggi = oggi.getDayOfMonth();
        int mmOggi = oggi.getMonthValue();
        //Controllo se esiste già bollo e Assicurazione inseriti per quell'anno
        //Pesca tutti i Veicoli di Proprietà
        List<VeicoloProprieta> lVeicoloProprieta = veicoloProprietaRepository.findAllActive(false);
        //iterator per creare bollo e Assicurazione
        Iterator itr = lVeicoloProprieta.iterator();
        while(itr.hasNext()) {
            VeicoloProprieta vp = (VeicoloProprieta) itr.next();
            if(!vp.getMotivazionePerditaProprieta().equals("Cancellazione Pra")) {
                Instant dataImmatricolazione =  vp.getDataImmatricolazione(); /// fare che crea Bollo da pagare
                int ggImmatricolazione;
                int mmImmatricolazione;
                //controlla se data è uguale a //dd/mm/yyyy o yyyy/mm/dd
                if(dataImmatricolazione.toString().substring(2,3).equals("/")){
                    ggImmatricolazione = Integer.parseInt(dataImmatricolazione.toString().substring(0,2));
                    mmImmatricolazione = Integer.parseInt(dataImmatricolazione.toString().substring(3,5));
                }
                else{
                    ggImmatricolazione = Integer.parseInt(dataImmatricolazione.toString().substring(8,10));
                    mmImmatricolazione = Integer.parseInt(dataImmatricolazione.toString().substring(5,7));
                }
                //int ggImmatricolazione = dataImmatricolazione.getDayOfMonth();
                //int mmImmatricolazione = dataImmatricolazione.getMonthValue();

                if(ggOggi == ggImmatricolazione && mmOggi == mmImmatricolazione){ /// controllare che gg/mm oggi è uguale a gg/mm Immatricolazione
                    Bollo bollo = new Bollo();
                    bollo.setVeicolo(vp.getVeicolo());
                    bollo.setDataScadenza(Instant.now());
                    bollo.setPagato(false);
                    bolloRepository.save(bollo);
                    String data = dataImmatricolazione.toString().substring(0,10);
                    String testo = "Oggi scade il bollo per l'auto ("+vp.getVeicolo().getTarga()+") in data:"+data+". \n \n Procedura Parco Auto CNR";
                    String mail = mailService.getEMailSedeDiAppartenenza(vp.getVeicolo().getResponsabile());

                    mailService.sendEmail(mail,"Oggi scade il bollo per l'auto",testo,false,false);


                }
                //crea assicurazione se dataAcquisto è uguale a dataOggi
                Instant dataAcquisto = vp.getDataAcquisto();
                int ggAcquisto;
                int mmAcquisto;
                //controlla se data è uguale a //dd/mm/yyyy o yyyy/mm/dd
                if(dataAcquisto.toString().substring(2,3).equals("/")){
                    ggAcquisto = Integer.parseInt(dataAcquisto.toString().substring(0,2));
                    mmAcquisto = Integer.parseInt(dataAcquisto.toString().substring(3,5));
                }
                else{
                    ggAcquisto = Integer.parseInt(dataAcquisto.toString().substring(8,10));
                    mmAcquisto = Integer.parseInt(dataAcquisto.toString().substring(5,7));
                }
                //int ggAcquisto = dataAcquisto.getDayOfMonth();
                //int mmAcquisto = dataAcquisto.getMonthValue();
                if(ggOggi == ggAcquisto && mmOggi == mmAcquisto){ /// controllare che gg/mm oggi è uguale a gg/mm Acquisto per assicurazione
                    AssicurazioneVeicolo assicurazioneVeicolo = new AssicurazioneVeicolo();
                    assicurazioneVeicolo.setVeicolo(vp.getVeicolo());
                    assicurazioneVeicolo.setDataScadenza(Instant.now());
                    assicurazioneVeicolo.setDataInserimento(Instant.now());
                    assicurazioneVeicolo.setCompagniaAssicurazione(" ");
                    assicurazioneVeicolo.setNumeroPolizza(" ");
                    assicurazioneVeicoloRepository.save(assicurazioneVeicolo);
                    String data = dataImmatricolazione.toString().substring(0,10);
                    String testo = "Oggi scade l'assicurazione per l'auto ("+vp.getVeicolo().getTarga()+") in data:"+data+". \n \n Procedura Parco Auto CNR";
                    String mail = mailService.getEMailSedeDiAppartenenza(vp.getVeicolo().getResponsabile());

                    mailService.sendEmail(mail,"Oggi scade l'assicurazione per l'auto",testo,false,false);

                }
            }
        }

    }


    @Scheduled(cron = "0 0 1 * * ?")
    public void noleggioControllo() {
        noleggio();
    }

    public void noleggio(){
        LocalDate oggi = LocalDate.now();
        //controllo che noleggio è quasi terminato.
        //Pesca tutti i Veicoli a Noleggio
        List<VeicoloNoleggio> lVeicoloNoleggio = veicoloNoleggioRepository.findAllActive(false);
        Iterator itr = lVeicoloNoleggio.iterator();
        while(itr.hasNext()) {
            VeicoloNoleggio vn = (VeicoloNoleggio) itr.next();
            //Controlla scadenza veicolo noleggio
            Instant dataFineNoleggio = vn.getDataFineNoleggio();
            if(dataFineNoleggio.equals(oggi)){
                //Mandare email che ricorda che scade il noleggio oggi
                String data = dataFineNoleggio.toString().substring(0,10);
                String testo = "Oggi scade il noleggio per l'auto  ("+vn.getVeicolo().getTarga()+") in data:"+data+". \n \n Procedura Parco Auto CNR";
                String mail = mailService.getEMailSedeDiAppartenenza(vn.getVeicolo().getResponsabile());

                mailService.sendEmail(mail,"Oggi scade il noleggio per l'auto",testo,false,false);
            }
            Instant dataProroga = vn.getDataProroga();
            if(dataProroga.equals(oggi)){
                //Mandare email che ricorda che scade il la proroga del noleggio oggi
                String data = dataProroga.toString().substring(0,10);
                String testo = "Oggi scade la proroga del noleggio per l'auto  ("+vn.getVeicolo().getTarga()+") in data:"+data+". \n \n Procedura Parco Auto CNR";
                String mail = mailService.getEMailSedeDiAppartenenza(vn.getVeicolo().getResponsabile());

                mailService.sendEmail(mail,"Oggi scade la proroga del noleggio per l'auto",testo,false,false);
            }
        }
    }
    public static <T> T json2Java(String fileName, Class<T> classType){
        T t = null;
        File file = new File("src/main/resources/"+ fileName);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            t=mapper.readValue(file, classType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
