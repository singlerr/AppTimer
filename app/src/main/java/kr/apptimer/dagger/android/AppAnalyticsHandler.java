/*
Copyright 2022 singlerr

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of singlerr nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package kr.apptimer.dagger.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import javax.inject.Inject;
import javax.inject.Singleton;
import kr.apptimer.database.data.ApplicationStats;
import kr.apptimer.database.utils.DataNotFoundException;

/***
 * Handler for {@link com.google.firebase.database.DatabaseReference} specially for {@link kr.apptimer.database.data.InstalledApplication}
 * @author Singlerr
 */
@Singleton
public final class AppAnalyticsHandler {

    private DatabaseReference database;

    @Inject
    public AppAnalyticsHandler(DatabaseReference database) {
        this.database = database;
    }

    /***
     * Add new {@link ApplicationStats} information to database
     * @param info {@link ApplicationStats}
     */
    public void submitAppInformation(
            ApplicationStats info,
            @Nullable OnSuccessListener<Void> successListener,
            @Nullable OnFailureListener failureListener) {

        if (successListener == null) successListener = unused -> {};

        if (failureListener == null) failureListener = unused -> {};

        database.child("apps")
                .child(info.getPackageUri().replaceAll("\\.", "?"))
                .setValue(info)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void submitOrUpdateAppInformation(
            ApplicationStats info,
            @Nullable OnSuccessListener<Void> successListener,
            @Nullable OnFailureListener failureListener) {
        getAppInformation(
                info.getPackageUri(),
                applicationStats -> {
                    applicationStats.add(info);
                    submitAppInformation(applicationStats, successListener, failureListener);
                },
                e -> submitAppInformation(info, successListener, failureListener));
    }

    public void getAppInformation(
            String packageUri,
            @NonNull OnSuccessListener<ApplicationStats> successListener,
            @NonNull OnFailureListener failureListener) {
        database.child("apps")
                .child(packageUri.replaceAll("\\.", "?"))
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) successListener.onSuccess(dataSnapshot.getValue(ApplicationStats.class));
                    else failureListener.onFailure(new DataNotFoundException());
                })
                .addOnFailureListener(failureListener);
    }
}
