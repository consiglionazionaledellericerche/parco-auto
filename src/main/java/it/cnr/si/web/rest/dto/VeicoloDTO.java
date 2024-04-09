/*
 * Copyright (C) 2024 Consiglio Nazionale delle Ricerche
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

package it.cnr.si.web.rest.dto;

import com.opencsv.bean.CsvBindByName;
import it.cnr.si.config.CustomCsvBindByPosition;
import it.cnr.si.domain.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class VeicoloDTO {
    @CsvBindByName(column = "Targa")
    @CustomCsvBindByPosition(1)
    private final String targa;
    @CsvBindByName(column = "Marca")
    @CustomCsvBindByPosition(2)
    private final String marca;
    @CsvBindByName(column = "Modello")
    @CustomCsvBindByPosition(3)
    private final String modello;
    @CsvBindByName(column = "Cilindrata")
    @CustomCsvBindByPosition(4)
    private final String cilindrata;
    @CsvBindByName(column = "Cv Kw")
    @CustomCsvBindByPosition(5)
    private final String cvkw;
    @CsvBindByName(column = "Km Percorsi")
    @CustomCsvBindByPosition(6)
    private final String kmPercorsi;
    @CsvBindByName(column = "UO")
    @CustomCsvBindByPosition(7)
    private final String istituto;
    @CsvBindByName(column = "Responsabile")
    @CustomCsvBindByPosition(8)
    private final String responsabile;
    @CsvBindByName(column = "Istituto")
    @CustomCsvBindByPosition(9)
    private final String cdsuo;
    @CsvBindByName(column = "Tipologia Veicolo")
    @CustomCsvBindByPosition(10)
    private final String tipologiaVeicolo;
    @CsvBindByName(column = "Alimentazione Veicolo")
    @CustomCsvBindByPosition(11)
    private final String alimentazioneVeicolo;
    @CsvBindByName(column = "Classe Emissioni Veicolo")
    @CustomCsvBindByPosition(12)
    private final String classeEmissioniVeicolo;
    @CsvBindByName(column = "Utilizzo Bene Veicolo")
    @CustomCsvBindByPosition(13)
    private final String utilizzoBeneVeicolo;

    @CsvBindByName(column = "Noleggio - Società")
    @CustomCsvBindByPosition(14)
    private String noleggioSocieta;
    @CsvBindByName(column = "Noleggio - Data Inizio")
    @CustomCsvBindByPosition(15)
    private String noleggioDataInizio;
    @CsvBindByName(column = "Noleggio - Data Fine")
    @CustomCsvBindByPosition(16)
    private String noleggioDataFine;
    @CsvBindByName(column = "Noleggio - Data Proroga")
    @CustomCsvBindByPosition(17)
    private String noleggioDataProroga;
    @CsvBindByName(column = "Noleggio - Partita IVA")
    @CustomCsvBindByPosition(18)
    private String noleggioPartitaIVA;

    @CsvBindByName(column = "Proprietà - Data Acquisto")
    @CustomCsvBindByPosition(19)
    private String proprietaDataAcquisto;
    @CsvBindByName(column = "Proprietà - Regione Immatricolazione")
    @CustomCsvBindByPosition(20)
    private String proprietaRegione;
    @CsvBindByName(column = "Proprietà - Etichetta")
    @CustomCsvBindByPosition(21)
    private String proprietaEtichetta;

    public VeicoloDTO(Veicolo veicolo) {
        this.targa = veicolo.getTarga();
        this.marca = veicolo.getMarca();
        this.modello = veicolo.getModello();
        this.cilindrata = veicolo.getCilindrata();
        this.cvkw = veicolo.getCvKw();
        this.kmPercorsi = Optional.ofNullable(veicolo.getKmPercorsi()).map(String::valueOf).orElse("");
        this.istituto = veicolo.getIstituto();
        this.responsabile = veicolo.getResponsabile();
        this.cdsuo = veicolo.getCdsuo();
        this.tipologiaVeicolo = Optional.ofNullable(veicolo.getTipologiaVeicolo()).map(TipologiaVeicolo::getNome).orElse("");
        this.alimentazioneVeicolo = Optional.ofNullable(veicolo.getAlimentazioneVeicolo()).map(AlimentazioneVeicolo::getNome).orElse("");
        this.classeEmissioniVeicolo = Optional.ofNullable(veicolo.getClasseEmissioniVeicolo()).map(ClasseEmissioniVeicolo::getNome).orElse("");
        this.utilizzoBeneVeicolo = Optional.ofNullable(veicolo.getUtilizzoBeneVeicolo()).map(UtilizzoBeneVeicolo::getNome).orElse("");
        Optional.ofNullable(veicolo.getVeicoloNoleggio())
            .ifPresent(veicoloNoleggio -> {
                this.noleggioSocieta = veicoloNoleggio.getSocieta();
                this.noleggioDataInizio = Optional.ofNullable(veicoloNoleggio.getDataInizioNoleggio())
                    .map(instant -> DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(instant)).orElse("");
                this.noleggioDataFine = Optional.ofNullable(veicoloNoleggio.getDataFineNoleggio())
                    .map(instant -> DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(instant)).orElse("");
                this.noleggioDataProroga = Optional.ofNullable(veicoloNoleggio.getDataProroga())
                    .map(instant -> DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(instant)).orElse("");
                this.noleggioPartitaIVA = veicoloNoleggio.getPartitaIva();
            });
        Optional.ofNullable(veicolo.getVeicoloProprieta())
            .ifPresent(veicoloProprieta -> {
                this.proprietaRegione = veicoloProprieta.getRegioneImmatricolazione();
                this.proprietaDataAcquisto = Optional.ofNullable(veicoloProprieta.getDataAcquisto())
                    .map(instant -> DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(instant)).orElse("");
                this.proprietaEtichetta = veicoloProprieta.getEtichetta();
            });
    }

}
