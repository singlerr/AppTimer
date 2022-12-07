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
package kr.apptimer.android.activity.main.recycler;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import kr.apptimer.R;
import kr.apptimer.database.dao.InstalledApplicationDao;
import kr.apptimer.database.data.InstalledApplication;

/***
 * {@link androidx.recyclerview.widget.RecyclerView} adapter for installed application list view
 * @author Singlerr
 */
public final class AppViewAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private InstalledApplicationDao database;

    private List<InstalledApplication> applicationList;

    private PackageManager packageManager;

    public AppViewAdapter(InstalledApplicationDao database, PackageManager packageManager) {
        this.database = database;
        this.applicationList = new ArrayList<>();
        this.packageManager = packageManager;

        database.findAll().observeOn(Schedulers.io()).subscribe(apps -> {
            applicationList = apps;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_app_icon, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        InstalledApplication application = applicationList.get(position);

        try {
            Drawable icon = packageManager.getApplicationIcon(application.getPackageUri());
            holder.getIconImageView().setImageDrawable(icon);
            holder.setPackageUri(application.getPackageUri());

            holder.getIconImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int alpha = holder.getIconImageView().getBackground().getAlpha() == 255 ? 128 : 255;
                    holder.getIconImageView().getBackground().setAlpha(alpha);
                    holder.setSelected(!holder.isSelected());
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        database.findAll().observeOn(Schedulers.io()).subscribe(apps -> {
            applicationList = apps;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }
}
