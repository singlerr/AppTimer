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
package kr.apptimer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import kr.apptimer.database.data.InstalledApplication;

/***
 * DAO class of installed application. Do transactions like storing application
 * install history {@link kr.apptimer.database.data.InstalledApplication}
 *
 * @author Singlerr
 */
@Dao
public interface InstalledApplicationDao {
  /***
   * Returns all {@link InstalledApplication} stored in database
   *
   * @return all {@link InstalledApplication} stored in database
   */
  @Query("SELECT * FROM installedapplication")
  List<InstalledApplication> findAll();

  /***
   * Returns unique {@link InstalledApplication} by {@param name}
   *
   * @param name
   *            name of {@link InstalledApplication}
   * @return {@link InstalledApplication} with {@param name}
   */
  @Query("SELECT * FROM installedapplication WHERE app_name = :name")
  InstalledApplication findByName(String name);

  /***
   * Returns unique {@link InstalledApplication} by {@param id}
   *
   * @param id
   *            id(primary key) of {@link InstalledApplication}
   * @return {@link InstalledApplication} with {@param id}
   */
  @Query("SELECT * FROM installedapplication WHERE id = :id")
  InstalledApplication findById(int id);

  /***
   * Insert new {@link InstalledApplication} to database Do not insert
   * {@link InstalledApplication} with same name or same id
   *
   * @param installedApplication
   *            {@link InstalledApplication} to insert
   */
  @Insert
  void insert(InstalledApplication installedApplication);

  /***
   * Delete existing {@link InstalledApplication} from database.
   *
   * @param installedApplication
   *            {@link InstalledApplication} to delete
   */
  @Delete
  void delete(InstalledApplication installedApplication);

  /***
   * Insert new {@link InstalledApplication} to database Do not insert
   * {@link InstalledApplication} with same name or same id
   *
   * @param installedApplications
   *            {@link InstalledApplication}(s) to insert
   */
  @Insert
  void insertAll(InstalledApplication... installedApplications);
}
