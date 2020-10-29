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

import it.cnr.si.domain.Veicolo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Veicolo.
 */
public class VeicoloPrintDto implements Serializable {
    private String anno;
    private List<VeicoloDetailPrintDto> veicolos;

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public List<VeicoloDetailPrintDto> getVeicolos() {
        return veicolos;
    }

    public void setVeicolos(List<VeicoloDetailPrintDto> veicolos) {
        this.veicolos = veicolos;
    }
}
