package kr.apptimer.android.page;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.apptimer.R;

public class Second extends Fragment{
    public static Second single;
    public static Second instance(){
        if(single==null)
            single=new Second();
        return single;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstance){
        View view=inflater.inflate(R.layout.activity_main4,container,false);
        return view;
    }
}
