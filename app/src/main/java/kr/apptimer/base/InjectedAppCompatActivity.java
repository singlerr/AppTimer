package kr.apptimer.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.apptimer.dagger.context.ApplicationContext;

/***
 * Base activity class
 * @author Singlerr
 */
public abstract class InjectedAppCompatActivity extends AppCompatActivity {

    public abstract void onActivityCreate(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getContext().inject(this);
        super.onCreate(savedInstanceState);
        onActivityCreate(savedInstanceState);
    }

    protected ApplicationContext getContext(){
        return ((InjectApplicationContext)getApplication()).getContext();
    }
}
