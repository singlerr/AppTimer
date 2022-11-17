package kr.apptimer.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Toast;

import android.content.Intent;

import kr.apptimer.android.service.AppExpirationOverlayService;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.context.ActivityContext;

public class ReservationCancellationActivity extends InjectedAppCompatActivity implements View.OnClickListener{
    Button removeButton;
    Button addButton;
    GridLayout gridLayout;
    Drawable backgroundDrawable;
    AlertDialog alertDialog;
    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        //Show overlay
        Intent service = new Intent(this, AppExpirationOverlayService.class);
        startService(service);

        addButton = findViewById(kr.apptimer.R.id.addButton);
        removeButton = findViewById(kr.apptimer.R.id.removeButton);
        gridLayout = findViewById(kr.apptimer.R.id.layout_1);
        backgroundDrawable = getDrawable(kr.apptimer.R.drawable.icon_basic);
    }
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == alertDialog) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    CheckBox childView;
                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        childView = (CheckBox) gridLayout.getChildAt(i);
                        if (childView.isChecked()) {
                            gridLayout.removeView(childView);
                            i--;
                        }
                    }
                    Toast toast = Toast.makeText(ReservationCancellationActivity.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    Toast toast = Toast.makeText(ReservationCancellationActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    };

    public void onClick(View view) {
        if (view == addButton) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setBackground(backgroundDrawable);
            checkBox.setGravity(Gravity.TOP|Gravity.LEFT);
            gridLayout.addView(checkBox);
        } else if (view == removeButton) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("알림")
                    .setMessage("삭제 예정을 취소하시겠어요?")
                    .setPositiveButton("예",dialogListener)
                    .setNegativeButton("아니요",dialogListener);
            alertDialog=builder.create();
            alertDialog.show();

        }
    }
    @Override
    public void bindListeners() {
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }
    @Override
    protected void inject(ActivityContext context) {context.inject(this);}
}