package kr.apptimer.base;

import android.app.Application;

import kr.apptimer.dagger.context.ApplicationContext;

import kr.apptimer.dagger.context.DaggerApplicationContext;
import lombok.Getter;

/***
 * This context lives in lifecycle
 * @author Singlerr
 */
public final class InjectApplicationContext extends Application {

    @Getter
    private static InjectApplicationContext instance;

    @Getter
    private final ApplicationContext context = DaggerApplicationContext.create();


    public InjectApplicationContext(){

        instance = this;
    }
}
