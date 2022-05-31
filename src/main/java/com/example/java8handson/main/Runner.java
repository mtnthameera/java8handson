package com.example.java8handson.main;

import com.example.java8handson.model.EmpMapper;
import com.example.java8handson.model.Employee;
import com.example.java8handson.model.TechnicalLead;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Nipun on 30/5/22
 */

public class Runner {

    public static void main(String[] args) {


                //Read from CSV file.
                List<Employee> employeeList = readFile();

                //Top 5 emp by DOJ
                //System.out.println(getTopFiveEmployees(employeeList));

                //Emp by DOJ
                //System.out.println(getEmpBasedOnDOJ(employeeList, LocalDateTime.of(2020, 12,30, 15,00 )));

                //Group Managers
                //System.out.println(groupManagers(employeeList));

                //Pop TL
               // System.out.println(populateTechLeads(employeeList));

                //Business Days
                System.out.println(calculateBusinessDaysInNextWeek(LocalDate.of(2022, 06, 1),
                        LocalDate.of(2022, 06, 8),
                        Optional.ofNullable(Arrays.asList(LocalDate.of(2022, 06, 12)))));
    }

    /**
     * Returns top 5 employees based on DOJ.
     *
     * @param employees
     * @return
     */
    private static List<Employee> getTopFiveEmployees(List<Employee> employees) {

        return employees.stream()
                .sorted(Comparator.comparing(Employee::getJoiningDate).reversed())
                .limit(5)
                .collect(Collectors.toList());

    }


    /**
     * Return list of employees joined on give date time.
     *
     * @param employees
     * @param dateTime
     * @return
     */
    private static List<Employee> getEmpBasedOnDOJ(List<Employee> employees, LocalDateTime dateTime) {

        return employees.stream()
                .filter(employee -> employee.getJoiningDate().isEqual(dateTime))
                .collect(Collectors.toList());
    }


    /**
     * Categorize employees as Regular and Manager based on their isManager field
     *
     * @param employees
     * @return
     */
    private static Map<String, List<Employee>> groupManagers(List<Employee> employees) {

        return employees.stream()
                .collect(Collectors.groupingBy(employee -> employee.isManager() ? "Manager" : "Regular"));

    }


    /**
     * Populate list with Object TechnicalLead for Employees having experience >7 yrs.
     *
     * @param employees
     * @return
     */
    private static List<TechnicalLead> populateTechLeads(List<Employee> employees) {

        return employees.stream()
                .filter(employee -> employee.getJoiningDate().isBefore(LocalDateTime.now().minusYears(7)))
                .map(employee -> new TechnicalLead(employee.getEmpId(), employee.getName(), employee.getJoiningDate()))
                .collect(Collectors.toList());
    }


    /**Business days in next week
     *
     * @param startDate
     * @param endDate
     * @param holidays
     * @return
     */
    private static List<LocalDate> calculateBusinessDaysInNextWeek(LocalDate startDate, LocalDate endDate, Optional<List<LocalDate>> holidays) {

        Predicate<LocalDate> isWeekEnd = date -> DayOfWeek.SATURDAY.equals(date.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(date.getDayOfWeek());
        Predicate<LocalDate> isHoliday = date -> holidays.isPresent() && holidays.get().contains(date);
        long noOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        List<LocalDate> workingDays = Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(noOfDaysBetween)
                .filter(isHoliday.or(isWeekEnd).negate())
                .collect(Collectors.toList());
        return workingDays;


    }


    private static List<Employee> readFile() {
        String fileName = "employee.csv";

        try {
            List<EmpMapper> empMappers = new CsvToBeanBuilder(new FileReader(fileName))
                    .withType(EmpMapper.class)
                    .build()
                    .parse();

           return empMappers.stream().map(mapper -> new Employee(mapper.getEmpId(),
                    mapper.getName(),
                    LocalDateTime.parse(mapper.getJoiningDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    mapper.getDesignation(),
                    mapper.isManager())).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}