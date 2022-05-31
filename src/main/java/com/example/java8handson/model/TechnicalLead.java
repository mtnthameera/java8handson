package com.example.java8handson.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Nipun on 30/5/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalLead {

    private String empId;
    private String name;
    private LocalDateTime dateOfJoin;

}
