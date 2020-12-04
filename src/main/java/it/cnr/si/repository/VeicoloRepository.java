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

package it.cnr.si.repository;

import it.cnr.si.domain.Veicolo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Veicolo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {
    @Query("SELECT v FROM Veicolo v where v.istituto in :istituto AND v.deleted =:deleted")
    Page<Veicolo> findByIstitutoStartsWithAndDeleted(@Param("istituto") List<String> istituto, @Param("deleted") Boolean deleted, Pageable pageable);
    @Query("SELECT v FROM Veicolo v where v.istituto in :istituto AND v.deleted =:deleted")
    List<Veicolo> findByIstitutoStartsWithAndDeleted(@Param("istituto") List<String> istituto, @Param("deleted") Boolean deleted);

    List<Veicolo> findByTargaAndDeleted(String targa, Boolean deleted);

    Page<Veicolo> findByDeletedFalse(Pageable pageable);

    List<Veicolo> findByDeletedFalse();
}
