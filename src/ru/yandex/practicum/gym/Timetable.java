package ru.yandex.practicum.gym;

import com.sun.source.tree.Tree;

import java.util.*;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable(){
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay,List<TrainingSession>> dayTable = timetable.get(day);

        if(dayTable == null){
            dayTable = new TreeMap<>();
            timetable.put(day, dayTable);
        }

        List<TrainingSession>sessionTrainTeime = dayTable.get(time);

        if(sessionTrainTeime == null){
            sessionTrainTeime = new ArrayList<>();
            dayTable.put(time, sessionTrainTeime);
        }

        sessionTrainTeime.add(trainingSession);
        //сохраняем занятие в расписании
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        ArrayList<TrainingSession> allTrain = new ArrayList<>();
        TreeMap<TimeOfDay, List<TrainingSession>> trainOfDay = timetable.get(dayOfWeek);

        if (trainOfDay == null){
            return allTrain;
        }else {
            for (List<TrainingSession> sessions : trainOfDay.values()) {
                allTrain.addAll(sessions);
            }
            return allTrain;
        }
        }
        //как реализовать, тоже непонятно, но сложность должна быть О(1)


    public List<TrainingSession> getTrainingSessionsForDayAndTime
            (DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> traning = timetable.get(dayOfWeek);
        ArrayList<TrainingSession> trainForTime = new ArrayList<>();

        if(traning == null){
            return trainForTime;
        }else {
            if (traning.get(timeOfDay) != null){
                for (TrainingSession train : traning.get(timeOfDay)) {
                    trainForTime.add(train);
                }
            }
            return trainForTime;
        }
    }
        //как реализовать, тоже непонятно, но сложность должна быть О(1)

    public List<CounterOfTrainings> getCountByCoaches(){
        HashMap<Coach, Integer> countByCoaches = new HashMap<>();
        ArrayList<CounterOfTrainings> result = new ArrayList<>();
        if(timetable != null){
          for(TreeMap<TimeOfDay, List<TrainingSession>>trainDay : timetable.values()){
              if(trainDay != null) {
                  for(List<TrainingSession>session : trainDay.values()){
                      if(session != null){
                          for(TrainingSession coaches : session){
                              if(countByCoaches.containsKey(coaches.getCoach())){
                                  countByCoaches.put(coaches.getCoach(),
                                          countByCoaches.get(coaches.getCoach()) + 1 );
                              }
                          }
                      }
                  }
              }
          }
          for(Map.Entry<Coach, Integer>entry : countByCoaches.entrySet()){
              result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
            }
          result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount() ));
          return result;
        }else{
            return result;
        }
    }

    public static class CounterOfTrainings{
        private final Coach coach;
        private final int count;

        public CounterOfTrainings(Coach coach, int count){
            this.coach = coach;
            this.count = count;
        }

            public Coach getCoach() {return coach;}
            public int getCount() {return count;}
        }



}
