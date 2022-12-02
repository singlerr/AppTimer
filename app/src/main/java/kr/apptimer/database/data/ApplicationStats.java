package kr.apptimer.database.data;

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
    private String applicationName;

    /***
     * Package uri of application
     */
    private String packageUri;

    /***
     * Use count per {@link DueCategory}
     */
    private Map<DueCategory,Integer> dueTimeCounts;

    public static enum DueCategory{
        SHORT,
        MEDIUM,
        LONG
    }
}
