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

import dagger.Subcomponent;
import kr.apptimer.android.activity.ReservationCancellationActivity;
import kr.apptimer.android.activity.SettingsActivity;
import kr.apptimer.android.activity.intro.IntroSlideActivity;
import kr.apptimer.android.activity.main.PermissionPage;
import kr.apptimer.android.activity.main.ReservationCancelPage;
import kr.apptimer.android.activity.main.ReservedAppListActivity;
import kr.apptimer.android.activity.main.Slider;
import kr.apptimer.android.activity.main.Statistics;
import kr.apptimer.android.activity.main.StatisticsPage;
import kr.apptimer.android.activity.permission.PermissionRequestActivity;
import kr.apptimer.base.InjectedAppCompatActivity;

/***
 * Subcomponent for activity classes which extend {@link kr.apptimer.base.InjectedAppCompatActivity}
 * Handle injecting in child classes of {@link kr.apptimer.base.InjectedAppCompatActivity}
 * @apiNote if you need to inject any subclass of {@link InjectedAppCompatActivity}, declare new inject method
 * @author Singlerr
 */
@Subcomponent
public interface ActivityContext {

    /***
     * 1. To create subcomponent, creating factory class manually is must.
     * @see kr.apptimer.dagger.module.ActivityContextModule
     * Factory that is used to create instance of {@link ActivityContext}
     * @author Singlerr
     */
    @Subcomponent.Factory
    interface Factory {
        ActivityContext create();
    }

    /***
     * This tells Dagger that {@link SettingsActivity} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(SettingsActivity activity);

    /***
     * This tells Dagger that {@link ReservationCancellationActivity} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(ReservationCancellationActivity activity);

    /***
     * This tells Dagger that {@link PermissionRequestActivity} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(PermissionRequestActivity activity);

    /***
     * This tells Dagger that {@link IntroSlideActivity} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(IntroSlideActivity activity);

    /***
     * This tells Dagger that {@link ReservedAppListActivity} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(ReservedAppListActivity activity);

    /***
     * This tells Dagger that {@link StatisticsPage} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(StatisticsPage activity);

    /***
     * This tells Dagger that {@link PermissionPage} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(PermissionPage activity);

    /***
     * This tells Dagger that {@link ReservationCancelPage} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(ReservationCancelPage activity);

    /***
     * This tells Dagger that {@link Slider} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(Slider activity);

    /***
     * This tells Dagger that {@link Statistics} requests injection
     * so that fields with {@link javax.inject.Inject} become not null
     *
     * @param activity
     *            activity
     */
    void inject(Statistics activity);
}
