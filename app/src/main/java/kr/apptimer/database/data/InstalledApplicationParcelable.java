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

    public InstalledApplicationParcelable(String packageUri, String name) {
        this.packageUri = packageUri;
        this.name = name;
    }

    public InstalledApplicationParcelable(InstalledApplication installedApplication) {
        this(installedApplication.getPackageUri(), installedApplication.getName());
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
            InstalledApplicationParcelable data =
                    new InstalledApplicationParcelable(source.readString(), source.readString());
            return data;
        }

        @Override
        public InstalledApplicationParcelable[] newArray(int size) {
            return new InstalledApplicationParcelable[size];
        }
    };
}
