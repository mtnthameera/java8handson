package com.example.java8handson.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
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
public class Employee {

    @CsvBindByPosition(position = 0)
    private String empId;
    @CsvBindByPosition(position = 1)
    private String name;
    @CsvBindByPosition(position = 2)
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joiningDate;
    @CsvBindByPosition(position = 3)
    private String designation;
    @CsvBindByPosition(position = 4)
    private boolean isManager;
}
