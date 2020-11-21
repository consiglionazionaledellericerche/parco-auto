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

import it.cnr.si.domain.AssicurazioneVeicolo;
import it.cnr.si.domain.Veicolo;
import it.cnr.si.domain.VeicoloProprieta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the AssicurazioneVeicolo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssicurazioneVeicoloRepository extends JpaRepository<AssicurazioneVeicolo, Long> {

    @Query("SELECT av FROM AssicurazioneVeicolo av where av.veicolo.istituto like :istituto AND av.veicolo.deleted =:deleted")
    public Page<AssicurazioneVeicolo> findByIstitutoStartsWithAndDeleted(@Param("istituto") String istituto, @Param("deleted") Boolean deleted, Pageable pageable);

    @Query("SELECT av FROM AssicurazioneVeicolo av where av.veicolo.deleted =:deleted ")
    public Page<AssicurazioneVeicolo> findByDeleted(@Param("deleted") Boolean deleted, Pageable pageable);

    Optional<AssicurazioneVeicolo> findByVeicolo(@Param("veicolo") Veicolo veicolo);

}