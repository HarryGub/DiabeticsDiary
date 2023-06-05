package com.shifuu.diabeticsdiary.mainactivity.foodsbase;

import android.app.Application;
import android.content.Context;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.util.Util;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FoodsbaseViewModel extends AndroidViewModel {

    AppRepository appRepository;

    public FoodsbaseViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public long insertFoodEntity(FoodEntity food)
    {
        return appRepository.insertFood(food);
    }

    public LiveData<FoodEntity> getFoodById(long foodId) {
        return appRepository.getFoodById(foodId);
    }

    public LiveData<List<FoodEntity>> getAllFoodEntitiesByUserSortedByName(long uid) {
        return appRepository.getAllFoodEntitiesByUserSortedByName(uid);
    }

    public long getUId(Context context) {
        return Util.getSharedPrefUserId(context);
    }

    public void deleteFoodById(long id) {
        appRepository.deleteFoodById(id);
    }

    public LiveData<List<FoodEntity>> getFoodEntitiesByUserByName(String foodsName, long uId) {
        return appRepository.getFoodEntitiesByUserByName(foodsName, uId);
    }
}