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

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CancellazionePra.
 */
@Entity
@Table(name = "cancellazione_pra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CancellazionePra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_consegna", nullable = false, columnDefinition="DATE")
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    private Instant dataConsegna;


    @Lob
    @Column(name = "documento_pra", nullable = false)
    private byte[] documentoPra;

    @Column(name = "documento_pra_content_type", nullable = false)
    private String documentoPraContentType;

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

    public Instant getDataConsegna() {
        return dataConsegna;
    }

    public CancellazionePra dataConsegna(Instant dataConsegna) {
        this.dataConsegna = dataConsegna;
        return this;
    }

    public void setDataConsegna(Instant dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    public byte[] getDocumentoPra() {
        return documentoPra;
    }

    public CancellazionePra documentoPra(byte[] documentoPra) {
        this.documentoPra = documentoPra;
        return this;
    }

    public void setDocumentoPra(byte[] documentoPra) {
        this.documentoPra = documentoPra;
    }

    public String getDocumentoPraContentType() {
        return documentoPraContentType;
    }

    public CancellazionePra documentoPraContentType(String documentoPraContentType) {
        this.documentoPraContentType = documentoPraContentType;
        return this;
    }

    public void setDocumentoPraContentType(String documentoPraContentType) {
        this.documentoPraContentType = documentoPraContentType;
    }

    public Veicolo getVeicolo() {
        return veicolo;
    }

    public CancellazionePra veicolo(Veicolo veicolo) {
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
        CancellazionePra cancellazionePra = (CancellazionePra) o;
        if (cancellazionePra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cancellazionePra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CancellazionePra{" +
            "id=" + getId() +
            ", dataConsegna='" + getDataConsegna() + "'" +
            ", documentoPra='" + getDocumentoPra() + "'" +
            ", documentoPraContentType='" + getDocumentoPraContentType() + "'" +
            "}";
    }
}
