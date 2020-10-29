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

import it.cnr.si.domain.LibrettoPercorrenzaVeicolo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LibrettoPercorrenzaVeicolo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibrettoPercorrenzaVeicoloRepository extends JpaRepository<LibrettoPercorrenzaVeicolo, Long> {

    // @Query("select form from Form form where form.processDefinitionKey =:processDefinitionKey and form.version = :version and form.taskId =:taskId")
    @Query("SELECT lpv FROM LibrettoPercorrenzaVeicolo lpv where lpv.veicolo.istituto like :istituto AND lpv.veicolo.deleted =:deleted")
    public Page<LibrettoPercorrenzaVeicolo> findByIstitutoStartsWithAndDeteled(@Param("istituto") String istituto, @Param("deleted") Boolean deleted, Pageable pageable);

    @Query("SELECT lpv FROM LibrettoPercorrenzaVeicolo lpv where lpv.veicolo.deleted =:deleted ")
    public Page<LibrettoPercorrenzaVeicolo> findByDeleted(@Param("deleted") Boolean deleted, Pageable pageable);
}
