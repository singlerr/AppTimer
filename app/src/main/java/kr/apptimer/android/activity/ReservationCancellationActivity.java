/*
Copyright 2022 singlerr

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of singlerr nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.context.ActivityContext;

/***
 * Activity which user can cancel application removal reservation
 */
public final class ReservationCancellationActivity extends InjectedAppCompatActivity
        implements View.OnClickListener, DialogInterface.OnClickListener {
    private Button removeButton;
    private Button addButton;
    private GridLayout gridLayout;
    private Drawable backgroundDrawable;
    private AlertDialog alertDialog;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        addButton = findViewById(kr.apptimer.R.id.addButton);
        removeButton = findViewById(kr.apptimer.R.id.removeButton);
        gridLayout = findViewById(kr.apptimer.R.id.layout_1);
        backgroundDrawable = getDrawable(kr.apptimer.R.drawable.icon_basic);
    }

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

    @Override
    public void onClick(View view) {
        if (view == addButton) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setBackground(backgroundDrawable);
            checkBox.setGravity(Gravity.TOP | Gravity.LEFT);
            gridLayout.addView(checkBox);
        } else if (view == removeButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("알림")
                    .setMessage("삭제 예정을 취소하시겠어요?")
                    .setPositiveButton("예", this)
                    .setNegativeButton("아니요", this);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void bindListeners() {
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    protected void inject(ActivityContext context) {
        context.inject(this);
    }
}
