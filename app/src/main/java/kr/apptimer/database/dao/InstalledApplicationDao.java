package kr.apptimer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kr.apptimer.database.data.InstalledApplication;

/***
 * DAO class of installed application.
 * Do transactions like storing application install history {@link kr.apptimer.database.data.InstalledApplication}
 * @author Singlerr
 */
@Dao
public interface InstalledApplicationDao {
    /***
     * Returns all {@link InstalledApplication} stored in database
     * @return all {@link InstalledApplication} stored in database
     */
    @Query("SELECT * FROM installedapplication")
    List<InstalledApplication> getAll();

    /***
     * Returns unique {@link InstalledApplication} by {@param name}
     * @param name name of {@link InstalledApplication}
     * @return {@link InstalledApplication} with {@param name}
     */
    @Query("SELECT * FROM installedapplication WHERE app_name = :name")
    InstalledApplication findByName(String name);

    /***
     * Returns unique {@link InstalledApplication} by {@param id}
     * @param id id(primary key) of {@link InstalledApplication}
     * @return {@link InstalledApplication} with {@param id}
     */
    @Query("SELECT * FROM installedapplication WHERE id = :id")
    InstalledApplication findById(int id);

    /***
     * Insert new {@link InstalledApplication} to database
     * Do not insert {@link InstalledApplication} with same name or same id
     * @param installedApplication {@link InstalledApplication} to insert
     */
    @Insert
    void insert(InstalledApplication installedApplication);

    /***
     * Delete existing {@link InstalledApplication} from database.
     * @param installedApplication {@link InstalledApplication} to delete
     */
    @Delete
    void delete(InstalledApplication installedApplication);

    /***
     * Insert new {@link InstalledApplication} to database
     * Do not insert {@link InstalledApplication} with same name or same id
     * @param installedApplications {@link InstalledApplication}(s) to insert
     */
    @Insert
    void insertAll(InstalledApplication... installedApplications);
}
