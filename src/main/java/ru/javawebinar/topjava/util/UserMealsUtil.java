package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        System.out.println(filteredByLocalTime(meals, LocalTime.of(7, 0), LocalTime.of(13, 0), 2000));

        meals.forEach(System.out::println);
        System.out.println("==================================");

        List<UserMealWithExcess> mealsTo = filteredByCycles1(meals,
                LocalTime.of(7, 0), LocalTime.of(21, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals,
                LocalTime.of(7, 0), LocalTime.of(21, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> tempMeals = new ArrayList<>();
        filteredByLocalTime(meals, startTime, endTime, 2000);

        return tempMeals;
    }


    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        System.out.println("TODO Implement by streams");
        int sumCaloriesPerDay = 0;

        List<UserMeal> stream = meals
                .stream()
                .filter(
                        n -> n.getDateTime().toLocalTime().isAfter(startTime) &&
                                n.getDateTime().toLocalTime().isBefore(endTime)
                )
//                .map(n -> n * n)
//                .sorted((o1, o2) -> -o1.compareTo(o2))
//                .limit(2)
                .collect(toList());
        System.out.println("===========stream===========");
        System.out.println(meals);
        System.out.println(stream);

        List<UserMealWithExcess> tempMeals = new ArrayList<>();

        for (UserMeal meal : stream) {
            sumCaloriesPerDay = sumCaloriesPerDay + meal.getCalories();
        }

        boolean excess = false;
        if (sumCaloriesPerDay > caloriesPerDay) {
            excess = true;
        }

        for (UserMeal meal : stream) {
            tempMeals.add(new UserMealWithExcess(LocalDateTime.now(), meal.getDescription(), meal.getCalories(), excess));
        }

        return tempMeals;
    }


    public static List<UserMeal> filteredByLocalTime(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        System.out.println("filteredByLocalTime");
        List<UserMealWithExcess> tempMealsWithExcess = new ArrayList<>();
        List<UserMeal> tempMeals = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                tempMeals.add(new UserMeal(meal.getDateTime(), meal.getDescription(), meal.getCalories()));
            }
        }
        boolean excess = excessFlag(tempMeals, caloriesPerDay);
        for (UserMeal meal : tempMeals) {
//            tempMealsWithExcess.add()
        }


        return tempMeals;
    }


    public static boolean excessFlag(List<UserMeal> meals, int caloriesPerDay) {
        System.out.println("excessFlag");
        int sumCaloriesPerDay = 0;
        for (UserMeal meal : meals) {
            sumCaloriesPerDay = sumCaloriesPerDay + meal.getCalories();
        }
        return sumCaloriesPerDay > caloriesPerDay;
    }

    public static List<UserMealWithExcess> filteredByCycles1(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            mapCalories.put(mealDate, mapCalories.getOrDefault(mealDate, 0) + meal.getCalories());
        }

        List<UserMealWithExcess> resultList = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDateTime dateTime = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(dateTime.toLocalTime(), startTime, endTime)) {
                resultList.add(new UserMealWithExcess(dateTime, meal.getDescription(), meal.getCalories(),
                        mapCalories.get(dateTime.toLocalDate()) > caloriesPerDay));
            }
        }
        System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssss");
        System.out.println(mapCalories);
        System.out.println(resultList);
        System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssss");
        return resultList;
    }


}