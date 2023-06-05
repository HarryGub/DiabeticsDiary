package com.shifuu.diabeticsdiary.database;

import android.app.Application;
import android.os.AsyncTask;

import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecDao;
import com.shifuu.diabeticsdiary.database.food_rec.FoodRecEntity;
import com.shifuu.diabeticsdiary.database.notes.NoteEntity;
import com.shifuu.diabeticsdiary.database.reminder.ReminderDao;
import com.shifuu.diabeticsdiary.database.reminder.ReminderEntity;
import com.shifuu.diabeticsdiary.database.sugar_rec.SugarRecEntity;
import com.shifuu.diabeticsdiary.database.user.UserEntity;
import com.shifuu.diabeticsdiary.mainactivity.home.sugar.SugarFragment;
import com.shifuu.diabeticsdiary.util.Util;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WholeDataHolder implements Serializable {
    private List<UserEntity> users;
    private List<FoodEntity> foods;
    private List<FoodRecEntity> foodRecs;
    private List<ReminderEntity> reminders;
    private List<SugarRecEntity> sugars;
    private List<NoteEntity> notes;

    public static final long serialVersionUID = 65296850982690L;

    private WholeDataHolder()
    {

    }

    private void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    private void setFoods(List<FoodEntity> foods) {
        this.foods = foods;
    }

    private void setFoodRecs(List<FoodRecEntity> foodRecs) {
        this.foodRecs = foodRecs;
    }

    private void setReminders(List<ReminderEntity> reminders) {
        this.reminders = reminders;
    }

    private void setSugars(List<SugarRecEntity> sugars) {
        this.sugars = sugars;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public List<FoodEntity> getFoods() {
        return foods;
    }

    public List<FoodRecEntity> getFoodRecs() {
        return foodRecs;
    }

    public List<ReminderEntity> getReminders() {
        return reminders;
    }

    public List<SugarRecEntity> getSugars() {
        return sugars;
    }

    public List<NoteEntity> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "WholeDataHolder{" +
                "users=" + users +
                ", foods=" + foods +
                ", foodRecs=" + foodRecs +
                ", reminders=" + reminders +
                ", sugars=" + sugars +
                ", notes=" + notes +
                '}';
    }

    public static WholeDataHolder get(Application application) throws InterruptedException, ExecutionException
    {
        return new GetWholeDataAsyncTask().execute(application).get();
    }


    private static class GetWholeDataAsyncTask extends AsyncTask<Application, Integer, WholeDataHolder>
    {

        @Override
        protected WholeDataHolder doInBackground(Application... applications) {
            AppRepository appRepository = new AppRepository(applications[0]);

            long uId = Util.getSharedPrefUserId(applications[0].getApplicationContext());

            List<UserEntity> users = appRepository.getAllUsersSync(uId);
            List<FoodEntity> foods = appRepository.getAllFoodEntitiesSync(uId);
            List<FoodRecEntity> foodRecs = appRepository.getAllFoodRecEntitiesSync(uId);
            List<ReminderEntity> reminders = appRepository.getAllRemindersSync(uId);
            List<SugarRecEntity> sugars = appRepository.getAllSugarRecsSync(uId);
            List<NoteEntity> notes = appRepository.getAllUsersNotesSync(uId);

            WholeDataHolder wholeDataHolder = new WholeDataHolder();

            wholeDataHolder.setFoodRecs(foodRecs);
            wholeDataHolder.setFoods(foods);
            wholeDataHolder.setSugars(sugars);
            wholeDataHolder.setReminders(reminders);
            wholeDataHolder.setUsers(users);
            wholeDataHolder.setNotes(notes);

            return wholeDataHolder;
        }
    }


}
