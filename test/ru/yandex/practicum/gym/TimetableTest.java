package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;

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

        //Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "В понедельник должно быть одно занятие.");
        assertTrue(mondaySessions.contains(singleTrainingSession), "Должна быть одна тренировка.");

        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession>tusdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertTrue(tusdaySessions.isEmpty(), "Во вторник не должно быть занятий.");
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
        List<TrainingSession>mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySession.size(), "Понедельник, должно быть одно занятие.");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession>thursdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySession.size(), "В четверг должно быть 2 занятия.");
        assertEquals(13, thursdaySession.get(0).getTimeOfDay().getHours(),
                "Тренировка должна начаться в 13:00.");
        assertEquals(20, thursdaySession.get(1).getTimeOfDay().getHours(),
                "Вторая тренировка должна начаться в 20:00.");

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySession.isEmpty(),"Во вторник не должно быть занятий.");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySession = timetable.getTrainingSessionsForDayAndTime
                (DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertEquals(1, mondaySession.size(), " понедельник 13:00 должно быть одно занятие.");
        assertTrue(mondaySession.contains(singleTrainingSession));

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> mondaySession14 = timetable.getTrainingSessionsForDayAndTime
                (DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondaySession14.isEmpty(), "В понедельник 14:00 нет занятий.");
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

        List<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        //
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

        //
        assertEquals(2, result.size(), "Должно быть два тренера");

        assertEquals(coach1, result.get(0).getCoach(), "Первым должен быть тренер с 3 тренировками");
        assertEquals(3, result.get(0).getCount(), "У первого тренера должно быть 3 тренировки");

        assertEquals(coach2, result.get(1).getCoach(), "Вторым должен быть тренер с 1 тренировкой");
        assertEquals(1, result.get(1).getCount(), "У второго тренера должна быть 1 тренировка");
    }

    @Test
    void testEmptyDayReturnsEmptyList() {
        Timetable timetable = new Timetable();

        List<TrainingSession> result = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        //
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

        List<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        result.clear();

        List<TrainingSession> original = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        //
        assertEquals(1, original.size(), "Расписание не должно измениться");
    }

}
