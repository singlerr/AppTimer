package kr.apptimer.dagger.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/***
 * Provider of {@link com.google.firebase.database.DatabaseReference}
 *
 * @apiNote {@link com.google.firebase.database.DatabaseReference} is singleton
 * @author Singlerr
 */
@Module
public final class FirebaseProvider {

    @Singleton
    @Provides
    public DatabaseReference provideFirebase(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
