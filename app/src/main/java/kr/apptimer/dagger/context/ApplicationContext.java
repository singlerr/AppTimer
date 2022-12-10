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
package kr.apptimer.dagger.context;

import dagger.Component;
import javax.inject.Singleton;
import kr.apptimer.android.receiver.AlarmRestorer;
import kr.apptimer.android.receiver.ApplicationInstallationReceiver;
import kr.apptimer.android.service.AppExpirationOverlayService;
import kr.apptimer.android.service.RemovalNotificationService;
import kr.apptimer.dagger.android.task.TaskExecutor;
import kr.apptimer.dagger.module.ActivityContextModule;
import kr.apptimer.dagger.module.ApplicationContextProvider;
import kr.apptimer.dagger.module.DatabaseProvider;
import kr.apptimer.dagger.module.FirebaseProvider;

@Singleton
@Component(
        modules = {
            DatabaseProvider.class,
            ApplicationContextProvider.class,
            ActivityContextModule.class,
            FirebaseProvider.class
        })
public interface ApplicationContext {

    /***
     * 3. To create subcomponent, you must create a function that exposes {@link kr.apptimer.dagger.context.ActivityContext.Factory}
     * @return factory of {@link ActivityContext}
     */
    ActivityContext.Factory activityContextFactory();

    /***
     * This tells Dagger that {@link ApplicationInstallationReceiver} requests
     * injection so that fields with {@link javax.inject.Inject} become not null
     *
     * @param receiver
     *            receiver instance
     */
    void inject(ApplicationInstallationReceiver receiver);
    /***
     * This tells Dagger that {@link AppExpirationOverlayService} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param service
     *            service instance
     */
    void inject(AppExpirationOverlayService service);

    /***
     * This tells Dagger that {@link AlarmRestorer} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param restorer
     *            boot broadcast receiver instance
     */
    void inject(AlarmRestorer restorer);

    /***
     * This tells Dagger that {@link TaskExecutor} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param executor
     *            task executor
     */
    void inject(TaskExecutor executor);

    /***
     * This tells Dagger that {@link RemovalNotificationService} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param service
     *            service
     */
    void inject(RemovalNotificationService service);
}
