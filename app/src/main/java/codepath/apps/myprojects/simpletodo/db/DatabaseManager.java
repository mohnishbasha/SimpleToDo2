package codepath.apps.myprojects.simpletodo.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import codepath.apps.myprojects.simpletodo.model.TodoItem;

public class DatabaseManager {

    private Context mContext;
    private ArrayList<TodoItem> items;

    public DatabaseManager(Context context) {
        mContext = context;
    }

    public long insert(TodoItem todoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConfig.COL_DATA, todoItem.getTodoText());
        return DataBaseHelper.getInstance(mContext).insert(DatabaseConfig.TABLE_TODO, contentValues);
    }

    public ArrayList<TodoItem> getItems() {
        // example of a raw query.
        // Cursor cursor = DbHelper.getInstance(mContext).execSQL("select * from " + DbConfig.TABLE_TODO, null);

        // alternatively you are also query as below.
        Cursor cursor = DataBaseHelper.getInstance(mContext).select(DatabaseConfig.TABLE_TODO, null, null, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        ArrayList<TodoItem> items = new ArrayList<TodoItem>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseConfig.COL_ID));
            String todoText = cursor.getString(cursor.getColumnIndex(DatabaseConfig.COL_DATA));
            TodoItem todoItem = new TodoItem(todoText);
            todoItem.setId(id);
            items.add(todoItem);
        }   // end while

        return items;
    }

    /**
     * Removes all the items from the table
     *
     * @return Number of rows affected.
     */
    public int removeAll() {
        return DataBaseHelper.getInstance(mContext).delete(DatabaseConfig.TABLE_TODO, null, null);
    }

    public long removeItem(int id) {
        String whereClause = DatabaseConfig.COL_ID + "=?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        return DataBaseHelper.getInstance(mContext).delete(DatabaseConfig.TABLE_TODO, whereClause, whereArgs);
    }
}
