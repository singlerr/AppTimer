package kr.apptimer.dagger.context;

import javax.inject.Singleton;

import dagger.Component;
import kr.apptimer.android.receiver.ApplicationInstallationReceiver;
import kr.apptimer.android.service.AppExpirationOverlayService;
import kr.apptimer.base.InjectedAppCompatActivity;
import kr.apptimer.dagger.module.ApplicationContextProvider;
import kr.apptimer.dagger.module.DatabaseProvider;

@Singleton
@Component(modules = {DatabaseProvider.class, ApplicationContextProvider.class})
public interface ApplicationContext {

    /***
     * This tells Dagger that {@link InjectedAppCompatActivity} requests injection so that fields with {@link javax.inject.Inject} become not null
     * @param activity activity
     */
    void inject(InjectedAppCompatActivity activity);

    /***
     * This tells Dagger that {@link ApplicationInstallationReceiver} requests injection so that fields with {@link javax.inject.Inject} become not null
     * @param receiver receiver instance
     */
    void inject(ApplicationInstallationReceiver receiver);
    /***
     * This tells Dagger that {@link AppExpirationOverlayService} requests injection so that fields with {@link javax.inject.Inject} become not null
     * @param service service instance
     */
    void inject(AppExpirationOverlayService service);
}
