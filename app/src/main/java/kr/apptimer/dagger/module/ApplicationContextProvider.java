package kr.apptimer.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kr.apptimer.base.InjectApplicationContext;

/***
 * Provider of {@link android.content.Context}
 * @apiNote {@link android.content.Context} is singleton
 * @author Singlerr
 */
@Module
public final class ApplicationContextProvider {

    @Singleton
    @Provides
    public Context provideContext(){
        return InjectApplicationContext.getInstance();
    }
}
