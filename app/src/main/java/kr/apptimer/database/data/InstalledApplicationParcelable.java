package kr.apptimer.database.data;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;

/***
 * Parcelable {@link InstalledApplication} for passing Serializable data via intent.
 * @see <a href="https://stackoverflow.com/a/41429570"></a>
 * @author Singlerr
 */
public final class InstalledApplicationParcelable implements Parcelable {

    @Getter
    private String packageUri;

    @Getter
    private String name;

    public InstalledApplicationParcelable(String packageUri, String name){
        this.packageUri = packageUri;
        this.name = name;
    }

    public InstalledApplicationParcelable(InstalledApplication installedApplication){
        this(installedApplication.getPackageUri(),installedApplication.getName());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageUri);
        dest.writeString(name);

    }

    public static Creator<InstalledApplicationParcelable> CREATOR = new Creator<InstalledApplicationParcelable>() {
        @Override
        public InstalledApplicationParcelable createFromParcel(Parcel source) {
            InstalledApplicationParcelable data = new InstalledApplicationParcelable(source.readString(),source.readString());
            return data;
        }

        @Override
        public InstalledApplicationParcelable[] newArray(int size) {
            return new InstalledApplicationParcelable[size];
        }
    };
}
