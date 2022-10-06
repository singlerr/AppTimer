package kr.apptimer.database.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/***
 * Saves information of installed application
 * Information could be: time when application installed, name of application etc.
 * @author Singlerr
 */
@Getter
@Setter
@Entity
public final class InstalledApplication {

    @PrimaryKey
    private int id;
    /***
     * Name of application
     */
    @NonNull
    @ColumnInfo(name = "app_name")
    private String name;
    /***
     * Time when application is newly installed
     */
    @NonNull
    @ColumnInfo(name = "app_installed_time")
    private Date time;
}
