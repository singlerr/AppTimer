package kr.apptimer.dagger.module;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kr.apptimer.database.LocalDatabase;

/***
 * Provider of {@link kr.apptimer.database.LocalDatabase}
 * @apiNote {@link kr.apptimer.database.LocalDatabase} is singleton
 * @author Singlerr
 */

@Module
public final class DatabaseProvider {

    @Singleton
    @Provides
    public LocalDatabase provideLocalDatabase(Context context){
        return Room.databaseBuilder(context,LocalDatabase.class,"appDatabase").build();
    }
}
