package com.shifuu.diabeticsdiary.mainactivity.foodsbase;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shifuu.diabeticsdiary.R;
import com.shifuu.diabeticsdiary.database.food.FoodEntity;
import com.shifuu.diabeticsdiary.util.Util;

public class FoodFragmentAdditionDialog {

    private FoodsbaseViewModel viewModel;
    private AlertDialog dialog = null;


    public FoodFragmentAdditionDialog(FoodsbaseViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    public void showAlertDialog(View parentView, LayoutInflater layoutInflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentView.getContext());

        final View layout = layoutInflater.inflate(R.layout.dialog_food_foodsbase, null);
        builder.setView(layout);


        EditText name = layout.findViewById(R.id.frag_food_dialog_first_row);
        EditText kal = layout.findViewById(R.id.frag_food_dialog_second_row);
        EditText portion = layout.findViewById(R.id.frag_food_dialog_third_row);
        TextView header = layout.findViewById(R.id.frag_food_dialog_header);

        EditText carbs = layout.findViewById(R.id.frag_food_dialog_carb_row);
        EditText protein = layout.findViewById(R.id.frag_food_dialog_protein_row);
        EditText fat = layout.findViewById(R.id.frag_food_dialog_fat_row);


        name.setText("");
        kal.setText("");
        portion.setText("");
        carbs.setText("");
        protein.setText("");
        fat.setText("");

        header.setText(R.string.frag_food_dialog_add_header);

        name.setHint(R.string.frag_food_dialog_food_name);
        kal.setHint(R.string.frag_food_dialog_food_kal);
        portion.setHint(R.string.frag_food_dialog_food_portion);
        carbs.setHint(R.string.frag_food_dialog_food_carb);
        protein.setHint(R.string.frag_food_dialog_food_protein);
        fat.setHint(R.string.frag_food_dialog_food_fat);


        Button applyButton = layout.findViewById(R.id.frag_food_dialog_confirm);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {

                    FoodEntity entity = new FoodEntity(
                            0,
                            "",
                            name.getText().toString(),
                            Integer.parseInt(kal.getText().toString()),
                            portion.getText().toString(),
                            viewModel.getUId(view.getContext()),
                            Integer.parseInt(carbs.getText().toString()),
                            Integer.parseInt(protein.getText().toString()),
                            Integer.parseInt(fat.getText().toString()));

                    viewModel.insertFoodEntity(entity);
                    Toast.makeText(parentView.getContext(), R.string.successfull_addition_food_dialog, Toast.LENGTH_LONG).show();
                    dialog.onBackPressed();
                }
                catch (RuntimeException e)
                {
                    Toast.makeText(parentView.getContext(), R.string.failed_addition_food_dialog, Toast.LENGTH_LONG).show();
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
