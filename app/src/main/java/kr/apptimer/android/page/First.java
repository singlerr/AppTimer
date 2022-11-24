package kr.apptimer.android.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import kr.apptimer.R;

public class First extends Fragment{
    public static First single;
    public static First instance(){
        if(single==null)
            single=new First();
        return single;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstance){
        View view=inflater.inflate(R.layout.activity_main2,container,false);
        return view;
    }
}
