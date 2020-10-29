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

package it.cnr.si.service.dto;

public class VeicoloDetailPrintDto {
    private String targa;
    private String istituto;
    private String responsabile;
    private String tipoProprieta;
    private String bollo;
    private String assicurazione;
    private String noleggio;

    public String getTarga() {
        return targa;
    }

    public VeicoloDetailPrintDto setTarga(String targa) {
        this.targa = targa;
        return this;
    }

    public String getIstituto() {
        return istituto;
    }

    public VeicoloDetailPrintDto setIstituto(String istituto) {
        this.istituto = istituto;
        return this;
    }

    public String getResponsabile() {
        return responsabile;
    }

    public VeicoloDetailPrintDto setResponsabile(String responsabile) {
        this.responsabile = responsabile;
        return this;
    }

    public String getTipoProprieta() {
        return tipoProprieta;
    }

    public VeicoloDetailPrintDto setTipoProprieta(String tipoProprieta) {
        this.tipoProprieta = tipoProprieta;
        return this;
    }

    public String getBollo() {
        return bollo;
    }

    public VeicoloDetailPrintDto setBollo(String bollo) {
        this.bollo = bollo;
        return this;
    }

    public String getAssicurazione() {
        return assicurazione;
    }

    public VeicoloDetailPrintDto setAssicurazione(String assicurazione) {
        this.assicurazione = assicurazione;
        return this;
    }

    public String getNoleggio() {
        return noleggio;
    }

    public VeicoloDetailPrintDto setNoleggio(String noleggio) {
        this.noleggio = noleggio;
        return this;
    }
}
