package com.hicode.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedStoredProcedureQuery(name = "Car.getTotalCardsbyModelEntity",
        procedureName = "GET_TOTAL_CARS_BY_MODEL", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "model_in", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "count_out", type = Integer.class)})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String model;

    @Column
    private Integer year;

}
