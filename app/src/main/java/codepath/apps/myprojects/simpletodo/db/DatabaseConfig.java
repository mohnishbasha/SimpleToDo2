package codepath.apps.myprojects.simpletodo.db;

/**
 * Created by i835811 on 1/11/15.
 */
public class DatabaseConfig {

    public static String DATABASE_NAME = "todolist.db";
    public static int DATABASE_VERSION = 1;

    // Store the surveys into the table
    public static final String TABLE_TODO = "table_todo";

    public static final String COL_ID = "id";
    public static final String COL_DATA = "data";

    public static final String CREATE_TABLE_SURVEYS = "CREATE TABLE IF NOT EXISTS " + TABLE_TODO + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COL_DATA + " TEXT NOT NULL);";

}
