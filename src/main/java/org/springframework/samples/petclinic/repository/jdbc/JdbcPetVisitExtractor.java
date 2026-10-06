/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.samples.petclinic.model.Visit;

/**
 * Extracts owner pets and their visits from a JDBC result set.
 */
public class JdbcPetVisitExtractor implements ResultSetExtractor<List<JdbcPet>> {

    @Override
    public List<JdbcPet> extractData(ResultSet rs) throws SQLException {
        Map<Integer, JdbcPet> petsById = new HashMap<>();

        while (rs.next()) {
            int petId = rs.getInt("pet_id");
            JdbcPet pet = petsById.get(petId);
            if (pet == null) {
                pet = new JdbcPet();
                pet.setId(petId);
                pet.setName(rs.getString("name"));
                java.sql.Date birthDate = rs.getDate("birth_date");
                pet.setBirthDate(birthDate != null ? birthDate.toLocalDate() : null);
                pet.setTypeId(rs.getInt("type_id"));
                pet.setOwnerId(rs.getInt("owner_id"));
                petsById.put(petId, pet);
            }

            Integer visitId = rs.getObject("visit_id") == null ? null : rs.getInt("visit_id");
            if (visitId != null) {
                Visit visit = new Visit();
                visit.setId(visitId);
                java.sql.Date visitDate = rs.getDate("visit_date");
                visit.setDate(visitDate != null ? visitDate.toLocalDate() : null);
                visit.setDescription(rs.getString("description"));
                pet.addVisit(visit);
            }
        }

        return new ArrayList<>(petsById.values());
    }
}
