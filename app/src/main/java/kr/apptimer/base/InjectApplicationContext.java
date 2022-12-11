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
package kr.apptimer.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executors;
import kr.apptimer.android.receiver.ApplicationInstallationReceiver;
import kr.apptimer.dagger.context.ApplicationContext;
import kr.apptimer.dagger.context.DaggerApplicationContext;
import lombok.Getter;

/***
 * This context lives in lifecycle
 *
 * @author Singlerr
 */
public final class InjectApplicationContext extends Application {

    private BroadcastReceiver appInstallationReceiver;

    public static final String KEY_PACKAGE_URI = "pgkName";

    public static final String KEY_NAME = "name";

    @Getter
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Getter
    private static InjectApplicationContext instance;

    @Getter
    private final ApplicationContext context = DaggerApplicationContext.create();

    @Getter
    private static ListeningExecutorService executorService;

    public InjectApplicationContext() {
        instance = this;
        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appInstallationReceiver = registerReceiver();
    }

    private BroadcastReceiver registerReceiver() {
        BroadcastReceiver receiver = new ApplicationInstallationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");

        registerReceiver(receiver, filter);
        Log.d("context", "registered receiver");

        return receiver;
    }
}
