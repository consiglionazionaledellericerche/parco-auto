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

import java.util.Collections;
import java.util.List;

public class PrintRequestBody {
    public static String REPORT_DATA_SOURCE = "REPORT_DATA_SOURCE";
    private String report;
    private String nomeFile;
    private List<PrintParam> params;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public PrintRequestBody setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
        return this;
    }

    public List<PrintParam> getParams() {
        return params;
    }

    public void setParams(List<PrintParam> params) {
        this.params = params;
    }

    public static PrintRequestBody create(String report, String nomeFile, String jsonDatasource) {
        PrintRequestBody requestBody = new PrintRequestBody();
        requestBody.setReport(report);
        requestBody.setNomeFile(nomeFile);
        requestBody.setParams(Collections.singletonList(
            new PrintParam()
                .key(new PrintParamKey().nomeParam(REPORT_DATA_SOURCE))
                .valoreParam(jsonDatasource)
                .paramType(String.class.getTypeName())
        ));
        return requestBody;
    }
}
