package codepath.apps.myprojects.simpletodo.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.simpletodo.R;

import java.util.ArrayList;

import codepath.apps.myprojects.simpletodo.adapters.TodoListAdapter;
import codepath.apps.myprojects.simpletodo.db.DatabaseManager;
import codepath.apps.myprojects.simpletodo.model.TodoItem;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import nl.changer.android.opensource.Utils;

public class TodoListFragment extends Fragment {

    private static final String TAG = TodoListFragment.class.getSimpleName();

    private DatabaseManager mDbManager;

    private EditText mTodoData;
    private View mAdd;
    private ListView mTodoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDbManager = new DatabaseManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTodoData = (EditText) view.findViewById(R.id.todo_data);
        mAdd = view.findViewById(R.id.btn_add);
        mTodoList = (ListView) view.findViewById(R.id.todo_list);

        listItems();
        initOnAddClick();
    }

    private View.OnClickListener mOnItemDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();

            if (tag instanceof TodoItem) {
                TodoItem todoItem = (TodoItem) tag;
                confirmRemoval(todoItem);
            } else {
                Log.w(TAG, " Unexpected tag found. Expected " + TodoItem.class.getSimpleName() + " Found: " + tag.getClass());
            }

        }
    };

    private void confirmRemoval(final TodoItem todoItem) {
        DialogInterface.OnClickListener yesClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(todoItem);
            }
        };

        DialogInterface.OnClickListener noClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        Utils.showConfirmDialog(getActivity(), getActivity().getString(R.string.confirm_remove_item),
                yesClickListener, noClickListener);
    }

    /**
     * Removes the item from the table in the database and updates the UI
     *
     * @param todoItem
     */
    private void removeItem(@NonNull TodoItem todoItem) {
        long rowsAffected = mDbManager.removeItem(todoItem.getId());
        if (rowsAffected == -1) {
            Crouton.makeText(getActivity(), getActivity().getString(R.string.remove_item_failed), Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), getActivity().getString(R.string.remove_item_success), Style.INFO).show();
            ((ArrayAdapter) mTodoList.getAdapter()).remove(todoItem);            // remove the item to the list
            ((ArrayAdapter) mTodoList.getAdapter()).notifyDataSetChanged();      // update the UI.

            // show empty listview if there are no more items to be displayed.
            if (mTodoList.getAdapter().getCount() == 0) {
                listItems();
            }
        }
    }

    /**
     * Lists the items in the already saved to-do items
     */
    private void listItems() {
        ArrayList<TodoItem> todoItems = getItems();

        if (todoItems == null || todoItems.size() == 0) {
            TextView tv = (TextView) getView().findViewById(R.id.empty_view);
            mTodoList.setEmptyView(tv);
        } else {
            TodoListAdapter adapter = new TodoListAdapter(getActivity(), R.layout.list_item, todoItems, mOnItemDeleteListener);
            mTodoList.setAdapter(adapter);
        }
    }

    private void initOnAddClick() {
        if (mAdd != null) {
            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem();
                }
            });
        }
    }

    /**
     * Adds items to the table in the database and refreshes the UI.
     */
    private void addItem() {
        if (mTodoData.getText() == null || TextUtils.isEmpty(mTodoData.getText().toString())) {
            Crouton.makeText(getActivity(), getActivity().getString(R.string.message_item_not_empty), Style.INFO).show();
            return;
        }

        String todoText = mTodoData.getText().toString();
        TodoItem todoItem = new TodoItem(todoText);
        long rowId = mDbManager.insert(todoItem);
        todoItem.setId((int) rowId);

        if (rowId == -1) {
            // some issue in adding item.
            Crouton.makeText(getActivity(), getActivity().getString(R.string.add_item_failed), Style.ALERT).show();
        } else {
            // successfully added
            mTodoData.setText(null);
            Crouton.makeText(getActivity(), getActivity().getString(R.string.add_item_success), Style.INFO).show();

            if (mTodoList.getAdapter() == null) {
                // if the list was empty previously
                listItems();
            } else {
                ((ArrayAdapter) mTodoList.getAdapter()).insert(todoItem, 0);        // add the item to the top of the list
                ((ArrayAdapter) mTodoList.getAdapter()).notifyDataSetChanged();      // update the UI.
            }
        }
    }

    public ArrayList<TodoItem> getItems() {
        return mDbManager.getItems();
    }

    private void removeAllItems() {
        int rowsAffected = mDbManager.removeAll();

        if (rowsAffected == -1) {
            Crouton.makeText(getActivity(), getActivity().getString(R.string.message_remove_all_failed), Style.ALERT).show();
        } else {
            // removal was successful, update the UI.
            ((ArrayAdapter) mTodoList.getAdapter()).clear();    // clear the list in the UI.
            listItems();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_list) {
            removeAllItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}