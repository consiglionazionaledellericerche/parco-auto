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

package it.cnr.si.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A VeicoloProprieta.
 */
@Entity
@Table(name = "veicolo_proprieta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VeicoloProprieta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_immatricolazione", nullable = false)
    private Instant dataImmatricolazione;

    @NotNull
    @Column(name = "data_acquisto", nullable = false)
    private Instant dataAcquisto;

    @NotNull
    @Column(name = "regione_immatricolazione", nullable = false)
    private String regioneImmatricolazione;


    @Lob
    @Column(name = "libretto", nullable = false)
    private byte[] libretto;

    @Column(name = "libretto_content_type", nullable = false)
    private String librettoContentType;

    @Lob
    @Column(name = "certificato_proprieta")
    private byte[] certificatoProprieta;

    @Column(name = "certificato_proprieta_content_type")
    private String certificatoProprietaContentType;

    @Column(name = "data_perdita_proprieta")
    private Instant dataPerditaProprieta;

    @Column(name = "altra_motivazione_perdita_proprieta")
    private String altraMotivazionePerditaProprieta;

    @Lob
    @Column(name = "cancellazione_pra")
    private byte[] cancellazionePra;

    @Column(name = "cancellazione_pra_content_type")
    private String cancellazionePraContentType;

    @Column(name = "etichetta")
    private String etichetta;

    @ManyToOne
    @JsonIgnoreProperties("")
    private MotivazionePerditaProprieta motivazionePerditaProprieta;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Veicolo veicolo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataImmatricolazione() {
        return dataImmatricolazione;
    }

    public VeicoloProprieta dataImmatricolazione(Instant dataImmatricolazione) {
        this.dataImmatricolazione = dataImmatricolazione;
        return this;
    }

    public void setDataImmatricolazione(Instant dataImmatricolazione) {
        this.dataImmatricolazione = dataImmatricolazione;
    }

    public Instant getDataAcquisto() {
        return dataAcquisto;
    }

    public VeicoloProprieta dataAcquisto(Instant dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
        return this;
    }

    public void setDataAcquisto(Instant dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
    }

    public String getRegioneImmatricolazione() {
        return regioneImmatricolazione;
    }

    public VeicoloProprieta regioneImmatricolazione(String regioneImmatricolazione) {
        this.regioneImmatricolazione = regioneImmatricolazione;
        return this;
    }

    public void setRegioneImmatricolazione(String regioneImmatricolazione) {
        this.regioneImmatricolazione = regioneImmatricolazione;
    }

    public byte[] getLibretto() {
        return libretto;
    }

    public VeicoloProprieta libretto(byte[] libretto) {
        this.libretto = libretto;
        return this;
    }

    public void setLibretto(byte[] libretto) {
        this.libretto = libretto;
    }

    public String getLibrettoContentType() {
        return librettoContentType;
    }

    public VeicoloProprieta librettoContentType(String librettoContentType) {
        this.librettoContentType = librettoContentType;
        return this;
    }

    public void setLibrettoContentType(String librettoContentType) {
        this.librettoContentType = librettoContentType;
    }

    public byte[] getCertificatoProprieta() {
        return certificatoProprieta;
    }

    public VeicoloProprieta certificatoProprieta(byte[] certificatoProprieta) {
        this.certificatoProprieta = certificatoProprieta;
        return this;
    }

    public void setCertificatoProprieta(byte[] certificatoProprieta) {
        this.certificatoProprieta = certificatoProprieta;
    }

    public String getCertificatoProprietaContentType() {
        return certificatoProprietaContentType;
    }

    public VeicoloProprieta certificatoProprietaContentType(String certificatoProprietaContentType) {
        this.certificatoProprietaContentType = certificatoProprietaContentType;
        return this;
    }

    public void setCertificatoProprietaContentType(String certificatoProprietaContentType) {
        this.certificatoProprietaContentType = certificatoProprietaContentType;
    }

    public Instant getDataPerditaProprieta() {
        return dataPerditaProprieta;
    }

    public VeicoloProprieta dataPerditaProprieta(Instant dataPerditaProprieta) {
        this.dataPerditaProprieta = dataPerditaProprieta;
        return this;
    }

    public void setDataPerditaProprieta(Instant dataPerditaProprieta) {
        this.dataPerditaProprieta = dataPerditaProprieta;
    }

    public String getAltraMotivazionePerditaProprieta() {
        return altraMotivazionePerditaProprieta;
    }

    public VeicoloProprieta altraMotivazionePerditaProprieta(String altraMotivazionePerditaProprieta) {
        this.altraMotivazionePerditaProprieta = altraMotivazionePerditaProprieta;
        return this;
    }

    public void setAltraMotivazionePerditaProprieta(String altraMotivazionePerditaProprieta) {
        this.altraMotivazionePerditaProprieta = altraMotivazionePerditaProprieta;
    }

    public byte[] getCancellazionePra() {
        return cancellazionePra;
    }

    public VeicoloProprieta cancellazionePra(byte[] cancellazionePra) {
        this.cancellazionePra = cancellazionePra;
        return this;
    }

    public void setCancellazionePra(byte[] cancellazionePra) {
        this.cancellazionePra = cancellazionePra;
    }

    public String getCancellazionePraContentType() {
        return cancellazionePraContentType;
    }

    public VeicoloProprieta cancellazionePraContentType(String cancellazionePraContentType) {
        this.cancellazionePraContentType = cancellazionePraContentType;
        return this;
    }

    public void setCancellazionePraContentType(String cancellazionePraContentType) {
        this.cancellazionePraContentType = cancellazionePraContentType;
    }

    public String getEtichetta() {
        return etichetta;
    }

    public VeicoloProprieta etichetta(String etichetta) {
        this.etichetta = etichetta;
        return this;
    }

    public void setEtichetta(String etichetta) {
        this.etichetta = etichetta;
    }

    public MotivazionePerditaProprieta getMotivazionePerditaProprieta() {
        return motivazionePerditaProprieta;
    }

    public VeicoloProprieta motivazionePerditaProprieta(MotivazionePerditaProprieta motivazionePerditaProprieta) {
        this.motivazionePerditaProprieta = motivazionePerditaProprieta;
        return this;
    }

    public void setMotivazionePerditaProprieta(MotivazionePerditaProprieta motivazionePerditaProprieta) {
        this.motivazionePerditaProprieta = motivazionePerditaProprieta;
    }

    public Veicolo getVeicolo() {
        return veicolo;
    }

    public VeicoloProprieta veicolo(Veicolo veicolo) {
        this.veicolo = veicolo;
        return this;
    }

    public void setVeicolo(Veicolo veicolo) {
        this.veicolo = veicolo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VeicoloProprieta veicoloProprieta = (VeicoloProprieta) o;
        if (veicoloProprieta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), veicoloProprieta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VeicoloProprieta{" +
            "id=" + getId() +
            ", dataImmatricolazione='" + getDataImmatricolazione() + "'" +
            ", dataAcquisto='" + getDataAcquisto() + "'" +
            ", regioneImmatricolazione='" + getRegioneImmatricolazione() + "'" +
            ", libretto='" + getLibretto() + "'" +
            ", librettoContentType='" + getLibrettoContentType() + "'" +
            ", certificatoProprieta='" + getCertificatoProprieta() + "'" +
            ", certificatoProprietaContentType='" + getCertificatoProprietaContentType() + "'" +
            ", dataPerditaProprieta='" + getDataPerditaProprieta() + "'" +
            ", altraMotivazionePerditaProprieta='" + getAltraMotivazionePerditaProprieta() + "'" +
            ", cancellazionePra='" + getCancellazionePra() + "'" +
            ", cancellazionePraContentType='" + getCancellazionePraContentType() + "'" +
            ", etichetta='" + getEtichetta() + "'" +
            "}";
    }
}
