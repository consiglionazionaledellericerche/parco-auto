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

public class PrintParam {
    private PrintParamKey key;
    private String valoreParam;
    private String paramType;

    public PrintParamKey getKey() {
        return key;
    }

    public PrintParam key(PrintParamKey key) {
        this.key = key;
        return this;
    }

    public String getValoreParam() {
        return valoreParam;
    }

    public PrintParam valoreParam(String valoreParam) {
        this.valoreParam = valoreParam;
        return this;
    }

    public String getParamType() {
        return paramType;
    }

    public PrintParam paramType(String paramType) {
        this.paramType = paramType;
        return this;
    }

}
