package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Test;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessionsMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        // Собираем все тренировки из всех временных слотов в один список
        List<TrainingSession> mondaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : mondaySessionsMap.values()) {
            mondaySessions.addAll(sessions);
        }
        assertEquals(1, mondaySessions.size(), "В понедельник должно быть одно занятие.");
        assertTrue(mondaySessions.contains(singleTrainingSession), "Должна быть одна тренировка.");

        // Проверить, что за четверг не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessionsMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertTrue(thursdaySessionsMap.isEmpty(), "В четверг не должно быть занятий.");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> mondaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : mondayMap.values()) {
            mondaySessions.addAll(sessions);
        }
        assertEquals(1, mondaySessions.size(), "Понедельник, должно быть одно занятие.");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursdayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        // Собираем все тренировки в порядке возрастания времени (ключи отсортированы)
        List<TrainingSession> thursdaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : thursdayMap.values()) {
            thursdaySessions.addAll(sessions);
        }
        assertEquals(2, thursdaySessions.size(), "В четверг должно быть 2 занятия.");
        assertEquals(13, thursdaySessions.get(0).getTimeOfDay().getHours(),
                "Тренировка должна начаться в 13:00.");
        assertEquals(20, thursdaySessions.get(1).getTimeOfDay().getHours(),
                "Вторая тренировка должна начаться в 20:00.");

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdayMap.isEmpty(), "Во вторник не должно быть занятий.");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> monday13Map =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        // Карта должна содержать один ключ - 13:00, значение - список из одного элемента
        List<TrainingSession> monday13Sessions = monday13Map.get(new TimeOfDay(13, 0));
        assertNotNull(monday13Sessions, "Список не должен быть null");
        assertEquals(1, monday13Sessions.size(), "В понедельник 13:00 должно быть одно занятие.");
        assertTrue(monday13Sessions.contains(singleTrainingSession));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> monday14Map =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(monday14Map.isEmpty(), "В понедельник 14:00 нет занятий.");
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Group group1 = new Group("Акробатика", Age.CHILD, 60);
        Group group2 = new Group("Гимнастика", Age.CHILD, 45);
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        TrainingSession session1 = new TrainingSession(
                group1, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(
                group2, coach2, DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        TreeMap<TimeOfDay, List<TrainingSession>> resultMap =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        List<TrainingSession> result = resultMap.get(new TimeOfDay(10, 0));
        assertNotNull(result, "Список не должен быть null");
        assertEquals(2, result.size(), "В 10:00 должно быть две тренировки");
        assertTrue(result.contains(session1), "Список должен содержать первую тренировку");
        assertTrue(result.contains(session2), "Список должен содержать вторую тренировку");
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Сидоров", "Алексей", "Дмитриевич");
        Coach coach2 = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Тестовая группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size(), "Должно быть два тренера");

        assertEquals(coach1, result.get(0).getCoach(), "Первым должен быть тренер с 3 тренировками");
        assertEquals(3, result.get(0).getCount(), "У первого тренера должно быть 3 тренировки");

        assertEquals(coach2, result.get(1).getCoach(), "Вторым должен быть тренер с 1 тренировкой");
        assertEquals(1, result.get(1).getCount(), "У второго тренера должна быть 1 тренировка");
    }

    @Test
    void testEmptyDayReturnsEmptyList() {
        Timetable timetable = new Timetable();

        TreeMap<TimeOfDay, List<TrainingSession>> result = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        assertNotNull(result, "Не должен возвращать null");
        assertTrue(result.isEmpty(), "Для пустого дня должен вернуться пустой список");
    }


    @Test
    void testReturnedCopyDoesNotAffectOriginal() {
        Timetable timetable = new Timetable();
        Group group = new Group("Тест", Age.ADULT, 60);
        Coach coach = new Coach("Тестов", "Тест", "Тестович");
        TrainingSession session = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session);

        TreeMap<TimeOfDay, List<TrainingSession>> resultMap = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        List<TrainingSession> resultList = resultMap.get(new TimeOfDay(10, 0));
        assertNotNull(resultList);
        resultList.clear(); // изменяем полученный список

        // Повторно получаем данные и проверяем, что оригинал не изменился
        TreeMap<TimeOfDay, List<TrainingSession>> originalMap = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        List<TrainingSession> originalList = originalMap.get(new TimeOfDay(10, 0));
        assertEquals(1, originalList.size(), "Расписание не должно измениться");
    }

}
