package kr.apptimer.android.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.apptimer.R;


public class Third extends Fragment{
    public static Third single;
    public static Third instance(){
        if(single==null)
            single=new Third();
        return single;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstance){
        View view=inflater.inflate(R.layout.activity_main3,container,false);
        return view;
    }
}
