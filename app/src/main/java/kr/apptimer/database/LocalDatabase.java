package kr.apptimer.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import kr.apptimer.database.dao.InstalledApplicationDao;
import kr.apptimer.database.data.InstalledApplication;
import kr.apptimer.database.utils.DateConverter;

/***
 * Local database only storing {@link kr.apptimer.database.data.InstalledApplication}
 * @author Singlerr
 */
@Database(entities = InstalledApplication.class, version = 1)
@TypeConverters(DateConverter.class)
public abstract class LocalDatabase extends RoomDatabase {

    /***
     * Returns {@link InstalledApplicationDao} instance
     * @return {@link InstalledApplicationDao} instance
     */
    public abstract InstalledApplicationDao installedApplicationDao();
}
