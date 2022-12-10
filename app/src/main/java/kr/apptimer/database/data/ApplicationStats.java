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

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * Application due time statistics
 * Saved in google firebase
 * @author Singlerr
 */
@NoArgsConstructor
public final class ApplicationStats {

    /***
     * Name of application
     */
    @Getter
    @Setter
    @NonNull
    private String applicationName;

    /***
     * Package uri of application
     */
    @Getter
    @Setter
    @NonNull
    private String packageUri;

    /***
     * Use count per {@link DueCategory}
     */
    @Getter
    @Setter
    @NonNull
    private Map<String, Integer> dueTimeCounts;

    public ApplicationStats(
            @NonNull String applicationName, @NonNull String packageUri, @NonNull Map<String, Integer> dueTimeCounts) {
        this.applicationName = applicationName;
        this.packageUri = packageUri;
        this.dueTimeCounts = dueTimeCounts;

        if (!dueTimeCounts.containsKey(DueCategory.SHORT.toString()))
            dueTimeCounts.put(DueCategory.SHORT.toString(), 0);
        if (!dueTimeCounts.containsKey(DueCategory.MEDIUM.toString()))
            dueTimeCounts.put(DueCategory.MEDIUM.toString(), 0);
        if (!dueTimeCounts.containsKey(DueCategory.LONG.toString())) dueTimeCounts.put(DueCategory.LONG.toString(), 0);
    }

    public ApplicationStats(@NonNull InstalledApplication application, @NonNull Map<String, Integer> dueTimeCounts) {
        this(application.getName(), application.getPackageUri(), dueTimeCounts);
    }

    public DueCategory getMostCommon() {
        int max = 0;
        DueCategory dueCategory = null;
        for (Map.Entry<String, Integer> stat : dueTimeCounts.entrySet()) {
            if (stat.getValue() > max) {
                dueCategory = DueCategory.valueOf(stat.getKey());
                max = stat.getValue();
            }
        }
        return dueCategory;
    }

    public void add(Map<String, Integer> counts) {
        dueTimeCounts.put(
                DueCategory.SHORT.toString(),
                dueTimeCounts.get(DueCategory.SHORT.toString()) + counts.get(DueCategory.SHORT.toString()));
        dueTimeCounts.put(
                DueCategory.MEDIUM.toString(),
                dueTimeCounts.get(DueCategory.MEDIUM.toString()) + counts.get(DueCategory.MEDIUM.toString()));
        dueTimeCounts.put(
                DueCategory.LONG.toString(),
                dueTimeCounts.get(DueCategory.LONG.toString()) + counts.get(DueCategory.LONG.toString()));
    }

    public void add(ApplicationStats stats) {
        add(stats.getDueTimeCounts());
    }

    public enum DueCategory implements Serializable {
        SHORT(1),
        MEDIUM(2),
        LONG(3);

        @Getter
        private int typeId;

        public static final long SHORT_MILLIS = 30 * 60 * 1000;

        public static final long LONG_MILLIS = 2 * 60 * 60 * 1000;

        DueCategory(int typeId) {
            this.typeId = typeId;
        }

        public static DueCategory fromMillis(long millis) {

            if (millis <= SHORT_MILLIS) return DueCategory.SHORT;
            if (millis <= LONG_MILLIS) return DueCategory.MEDIUM;
            return DueCategory.LONG;
        }
    }
}
