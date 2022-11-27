package com.example.lab06;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public final class PackageAddedReceiver extends BroadcastReceiver implements View.OnClickListener {
    private Button buttonPositive;
    private Button buttonNegative;
    private Context broadcastReceiverContext;
    private EditText editDay;
    private EditText editHour;
    private EditText editMinute;
    private PackageManager packageManager;
    private String packageName;
    private String action;
    private View overLayView;
    private Window window;
    private WindowManager windowManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        broadcastReceiverContext = context;
        packageName = intent.getData().getEncodedSchemeSpecificPart();
        action = intent.getAction();
        //Thread thread = new Thread(){
        //    public void run(){
        //Looper.prepare();
        //handler = new Handler();
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {     //새로운 패키지가 설치됨을 감지했을 때
            Log.d(context.getPackageName(), "Package Added Detected!!");
            packageManager = context.getPackageManager();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER;
            overLayView = inflater.inflate(R.layout.activity_overlay_on_package_added, null);
            final ImageView appIcon = overLayView.findViewById(R.id.addedPackageIcon);
            try {
                appIcon.setImageDrawable(packageManager.getApplicationIcon(packageName));
            } catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.d(nameNotFoundException.toString(), nameNotFoundException.getMessage());
            }
            windowManager.addView(overLayView, params);
            buttonPositive = overLayView.findViewById(R.id.buttonPositive);
            buttonNegative = overLayView.findViewById(R.id.buttonNegative);
            editDay = overLayView.findViewById(R.id.editDay);
            editHour = overLayView.findViewById(R.id.editHour);
            editMinute = overLayView.findViewById(R.id.editMinute);
            buttonNegative.setOnClickListener(this);
            buttonPositive.setOnClickListener(this);
            editDay.setOnClickListener(this);
            editHour.setOnClickListener(this);
            editMinute.setOnClickListener(this);

            //window = ((Activity) overLayView.getContext()).getWindow();
    //        InputMethodManager imm = (InputMethodManager)broadcastReceiverContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    //        imm.toggleSoftInput(0,0);
        //    TextInputEditText input=new TextInputEditText(broadcastReceiverContext);
        /*    input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    }

                }
            });*/
            //오버레이 화면 띄워서 시간 설정 후 삭제 예약 리스트에 등록
        }
    }
    //};
    //      thread.start();
    //throw new UnsupportedOperationException("Not yet implemented");
    //}


    @Override
    public void onClick(View view) {
        if (view == buttonPositive) {
            try {
                if (Integer.getInteger(editDay.getText().toString()) >= 0
                        && Integer.getInteger(editHour.getText().toString()) >= 0
                        && Integer.getInteger(editHour.getText().toString()) <= 24
                        && Integer.getInteger(editMinute.getText().toString()) >= 0
                        && Integer.getInteger(editMinute.getText().toString()) <= 60) {
                    Toast.makeText(broadcastReceiverContext, "예약되었습니다.", Toast.LENGTH_SHORT).show();

                    windowManager.removeView(overLayView);
                } else
                    Toast.makeText(broadcastReceiverContext, "비정상적인 수치를 입력했습니다.", Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                Toast.makeText(broadcastReceiverContext, "빈 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                Log.d(e.toString(), e.getMessage());
            }
        } else if(view==buttonNegative) {
            Toast.makeText(broadcastReceiverContext, "취소되었습니다.", Toast.LENGTH_SHORT).show();

            windowManager.removeView(overLayView);
        }
        else if(view==editDay){
            InputMethodManager imm = (InputMethodManager)broadcastReceiverContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view,0);
        }
    }
}