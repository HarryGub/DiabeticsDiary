package com.shifuu.diabeticsdiary.mainactivity.usertransfer;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.WorkSource;
import android.util.Log;

import com.shifuu.diabeticsdiary.database.AppRepository;
import com.shifuu.diabeticsdiary.database.WholeDataHolder;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.util.Util;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.lifecycle.AndroidViewModel;

public class UserTransferViewModel extends AndroidViewModel {
    private long uid;
    private AppRepository appRepository;
    Application application;

    public UserTransferViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        this.application = application;
        uid = Util.getSharedPrefUserId(application.getApplicationContext());
    }


    public String writeUserImportBundle(Uri uri)
    {
        WholeDataHolder wholeDataHolder;

        try {
            wholeDataHolder = WholeDataHolder.get(application);

            Log.d("UserTransfer", wholeDataHolder.toString());

            writeSerial(wholeDataHolder, uri);


        } catch (InterruptedException e) {
            e.printStackTrace();
            return  "Формирование файла прервано";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  "Не удалось сформировать файл";
        } catch (IOException e) {
            e.printStackTrace();
            return  "Невозможно получить доступ к папке.";
        }

        return "Файл сохранён.";
    }

    private void writeSerial(WholeDataHolder holder, Uri uri) throws IOException {

        ParcelFileDescriptor fileDescriptor = application.getApplicationContext().getContentResolver().openFileDescriptor(uri, "w");

        FileOutputStream fout = new FileOutputStream(fileDescriptor.getFileDescriptor());
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(holder);
        oos.close();
        fout.close();
        fileDescriptor.close();
    }

    public String writeUserImportBundleXlsx(Uri uri) {
        WholeDataHolder wholeDataHolder;

        try {
            wholeDataHolder = WholeDataHolder.get(application);

            Log.d("UserTransfer", wholeDataHolder.toString());

            writeXlsx(wholeDataHolder, uri);


        } catch (InterruptedException e) {
            e.printStackTrace();
            return  "Формирование файла прервано";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  "Не удалось сформировать файл";
        } catch (IOException e) {
            e.printStackTrace();
            return  "Невозможно получить доступ к папке.";
        }

        return "Файл сохранён.";
    }

    private void writeXlsx(WholeDataHolder wholeDataHolder, Uri uri) throws IOException {
        ParcelFileDescriptor fileDescriptor = application.getApplicationContext().getContentResolver().openFileDescriptor(uri, "w");
        FileOutputStream fout = new FileOutputStream(fileDescriptor.getFileDescriptor());

        Workbook workbook = new Workbook(fout, "DiabeticsDiary", "1.01");
        Worksheet sugarWs = workbook.newWorksheet("Сахар");

        sugarWs.value(0, 0, "Дата");
        sugarWs.value(0, 1, "Время");
        sugarWs.value(0, 2, "Уровень сахара");
        sugarWs.value(0, 3, "Заметка");

        for (int i = 0; i < wholeDataHolder.getSugars().size(); i++) {
            sugarWs.value(i+1, 0, Util.dateFormatter.format(wholeDataHolder.getSugars().get(i).getDate()));
            sugarWs.value(i+1, 1, Util.timeFormatter.format(wholeDataHolder.getSugars().get(i).getTime()));
            sugarWs.value(i+1, 2, wholeDataHolder.getSugars().get(i).getValue());
            sugarWs.value(i+1, 3, wholeDataHolder.getSugars().get(i).getNote());
        }

        Worksheet notesWs = workbook.newWorksheet("Заметки");

        notesWs.value(0, 0, "Дата");
        notesWs.value(0, 1, "Время");
        notesWs.value(0, 2, "Заметка");

        for (int i = 0; i < wholeDataHolder.getNotes().size(); i++) {
            notesWs.value(i+1, 0, Util.dateFormatter.format(wholeDataHolder.getNotes().get(i).getDate()));
            notesWs.value(i+1, 1, Util.timeFormatter.format(wholeDataHolder.getNotes().get(i).getTime()));
            notesWs.value(i+1, 2, wholeDataHolder.getNotes().get(i).getNote());
        }

        Worksheet foodWs = workbook.newWorksheet("Продукты");

        foodWs.value(0, 0, "id");
        foodWs.value(0, 1, "Название");
        foodWs.value(0, 2, "Калорийность");
        foodWs.value(0, 3, "Белки");
        foodWs.value(0, 4, "Жиры");
        foodWs.value(0, 5, "Углеводы");
        foodWs.value(0, 6, "Порция");


        for (int i = 0; i < wholeDataHolder.getFoods().size(); i++) {
            foodWs.value(i + 1, 0, wholeDataHolder.getFoods().get(i).getId());
            foodWs.value(i + 1, 1, wholeDataHolder.getFoods().get(i).getName());
            foodWs.value(i + 1, 2, wholeDataHolder.getFoods().get(i).getKal());
            foodWs.value(i + 1, 3, wholeDataHolder.getFoods().get(i).getProtein());
            foodWs.value(i + 1, 4, wholeDataHolder.getFoods().get(i).getFat());
            foodWs.value(i + 1, 5, wholeDataHolder.getFoods().get(i).getCarbs());
            foodWs.value(i + 1, 6, wholeDataHolder.getFoods().get(i).getPortion());
        }


        Worksheet foodRecWs = workbook.newWorksheet("Записи о калориях");
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        foodRecWs.value(0, 0, "Дата");
        foodRecWs.value(0, 1, "Время");
        foodRecWs.value(0, 2, "Название продукта");
        foodRecWs.value(0, 3, "Масса от порции");
        foodRecWs.value(0, 4, "Порция");
        foodRecWs.value(0, 5, "Хлебные единицы");
        foodRecWs.value(0, 6, "Заметка");

        for (int i = 0; i < wholeDataHolder.getFoodRecs().size(); i++) {
            foodRecWs.value(i+1, 0, Util.dateFormatter.format(wholeDataHolder.getFoodRecs().get(i).getDate()));
            foodRecWs.value(i+1, 1, Util.timeFormatter.format(wholeDataHolder.getFoodRecs().get(i).getTime()));
            foodRecWs.value(i+1, 2, getFoodEntityById(wholeDataHolder, wholeDataHolder.getFoodRecs().get(i).getFoodId()).getName());
            foodRecWs.value(i+1, 3, wholeDataHolder.getFoodRecs().get(i).getMass());
            foodRecWs.value(i+1, 4, getFoodEntityById(wholeDataHolder, wholeDataHolder.getFoodRecs().get(i).getFoodId()).getPortion());
            foodRecWs.value(i+1, 5, decimalFormat.format(getFoodEntityById(wholeDataHolder, wholeDataHolder.getFoodRecs().get(i).getFoodId()).getCarbs()/10));
            foodRecWs.value(i+1, 6, wholeDataHolder.getFoodRecs().get(i).getNote());
        }


        notesWs.close();
        foodRecWs.close();
        foodWs.close();
        sugarWs.close();
        workbook.close();
        fout.close();
        fileDescriptor.close();

    }


    private FoodEntity getFoodEntityById(WholeDataHolder wholeDataHolder, long id)
    {
        return wholeDataHolder.getFoods().stream().filter(food -> food.getId() == id).collect(Collectors.toList()).get(0);
    }
}













