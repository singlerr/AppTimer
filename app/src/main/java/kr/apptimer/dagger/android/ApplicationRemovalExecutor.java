package kr.apptimer.dagger.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;
import javax.inject.Singleton;

import kr.apptimer.base.InjectApplicationContext;

/***
 * Call {@link android.content.Intent} to request removing android application(package).
 * Can be injected into other classes by dagger.
 * @author Singlerr
 */
@Singleton
public final class ApplicationRemovalExecutor {

    private Context context;

    @Inject
    public ApplicationRemovalExecutor(InjectApplicationContext context){
        this.context = context;
    }

    public void requestRemoval(String packageUri){
        requestRemoval(Uri.parse(packageUri));
    }

    public void requestRemoval(Uri uri){
        requestRemoval(context, new Intent(Intent.ACTION_DELETE,uri));
    }
    private void requestRemoval(Context context, Intent intent){
        context.startActivity(intent);
    }

}
