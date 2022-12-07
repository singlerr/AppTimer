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
package kr.apptimer.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import javax.inject.Inject;
import kr.apptimer.android.service.AppExpirationOverlayService;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.NotificationHelper;
import kr.apptimer.database.LocalDatabase;

/***
 * Receiver class for installing application
 *
 * @author Singlerr
 */
public final class ApplicationInstallationReceiver extends BroadcastReceiver {

    @Inject
    LocalDatabase database;

    @Inject
    NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) { // 새로운 패키지가 설치됨을 감지했을 때
            Log.d(context.getPackageName(), "Package Added Detected!!");

            String packageName = intent.getData().getEncodedSchemeSpecificPart();
            Intent serviceIntent = new Intent(context, AppExpirationOverlayService.class);
            serviceIntent.putExtra("pkgName", packageName);
            context.startService(serviceIntent);
        }
    }

    public ApplicationInstallationReceiver() {
        super();
        InjectApplicationContext.getInstance().getContext().inject(this);
    }
}
