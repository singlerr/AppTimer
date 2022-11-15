package kr.apptimer.dagger.android;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import kr.apptimer.database.data.InstalledApplication;

/***
 * Handler for {@link com.google.firebase.database.DatabaseReference} specially for {@link kr.apptimer.database.data.InstalledApplication}
 * @author Singlerr
 */
@Singleton
public final class AppAnalyticsHandler {

    private DatabaseReference database;

    @Inject
    public AppAnalyticsHandler(DatabaseReference database){
        this.database = database;
    }

    /***
     * Add new {@link InstalledApplication} information to database
     * @param info {@link InstalledApplication}
     */
    public void submitAppInformation(InstalledApplication info){
        database.push().setValue(info);
    }



}
