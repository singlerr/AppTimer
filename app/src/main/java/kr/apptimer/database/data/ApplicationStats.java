package kr.apptimer.database.data;

import androidx.annotation.NonNull;

import java.util.Map;

import lombok.Data;

/***
 * Application due time statistics
 * Saved in google firebase
 * @author Singlerr
 */
@Data
public final class ApplicationStats {

    /***
     * Name of application
     */
    @NonNull
    private String applicationName;

    /***
     * Package uri of application
     */
    @NonNull
    private String packageUri;

    /***
     * Use count per {@link DueCategory}
     */
    @NonNull
    private Map<DueCategory,Integer> dueTimeCounts;

    public static enum DueCategory{
        SHORT,
        MEDIUM,
        LONG
    }
}
