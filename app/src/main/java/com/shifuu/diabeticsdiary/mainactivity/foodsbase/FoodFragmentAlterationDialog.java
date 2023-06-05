package com.shifuu.diabeticsdiary.mainactivity.foodsbase;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;

public class FoodFragmentAlterationDialog {

    private FoodsbaseViewModel viewModel;
    private AlertDialog dialog = null;



    public FoodFragmentAlterationDialog(FoodsbaseViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    public void showAlertDialog(FoodEntity food, View parentView, LayoutInflater layoutInflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentView.getContext());

        final View layout = layoutInflater.inflate(R.layout.dialog_food_foodsbase, null);
        builder.setView(layout);


        EditText name = layout.findViewById(R.id.frag_food_dialog_first_row);
        EditText kal = layout.findViewById(R.id.frag_food_dialog_second_row);
        EditText portion = layout.findViewById(R.id.frag_food_dialog_third_row);
        EditText carbs = layout.findViewById(R.id.frag_food_dialog_carb_row);
        EditText protein = layout.findViewById(R.id.frag_food_dialog_protein_row);
        EditText fat = layout.findViewById(R.id.frag_food_dialog_fat_row);

        name.setText(food.getName());
        kal.setText(String.valueOf(food.getKal()));
        portion.setText(food.getPortion());
        carbs.setText(String.valueOf(food.getCarbs()));
        protein.setText(String.valueOf(food.getProtein()));
        fat.setText(String.valueOf(food.getFat()));

        Button applyButton = layout.findViewById(R.id.frag_food_dialog_confirm);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    FoodEntity entity = new FoodEntity(
                            food.getId(),
                            "",
                            name.getText().toString(),
                            Integer.parseInt(kal.getText().toString()),
                            portion.getText().toString(),
                            food.getUserId(),
                            Integer.parseInt(carbs.getText().toString()),
                            Integer.parseInt(protein.getText().toString()),
                            Integer.parseInt(fat.getText().toString()));

                    viewModel.insertFoodEntity(entity);
                    Toast.makeText(parentView.getContext(), R.string.successfull_alteraion_food_dialog, Toast.LENGTH_LONG).show();
                    dialog.onBackPressed();
                }
                catch (RuntimeException e)
                {
                    Toast.makeText(parentView.getContext(), R.string.failed_alteraion_food_dialog, Toast.LENGTH_LONG).show();
                }

            }
        });

        Button cancelButton = layout.findViewById(R.id.frag_food_dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), R.string.cancel_alteraion_food_dialog, Toast.LENGTH_LONG).show();
                dialog.onBackPressed();
            }
        });

        dialog = builder.create();

        dialog.show();
    }
}
