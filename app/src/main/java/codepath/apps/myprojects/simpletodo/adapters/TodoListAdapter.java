package codepath.apps.myprojects.simpletodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.simpletodo.R;

import java.util.ArrayList;

import codepath.apps.myprojects.simpletodo.model.TodoItem;

public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private View.OnClickListener mOnItemDeleteListener;

    public TodoListAdapter(Context context, int textViewResourceId, ArrayList<TodoItem> todoItems, View.OnClickListener onItemDeleteListener) {
        super(context, textViewResourceId, todoItems);
        mOnItemDeleteListener = onItemDeleteListener;
    }

    private static class ViewHolder {
        TextView mTodoText;
        ImageView mDeleteItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        TodoItem todoItem = getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder.mTodoText = (TextView) convertView.findViewById(R.id.todo_data);
            holder.mDeleteItem = (ImageView) convertView.findViewById(R.id.delete_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTodoText.setText(todoItem.getTodoText());

        holder.mDeleteItem.setOnClickListener(mOnItemDeleteListener);
        holder.mDeleteItem.setTag(todoItem);    // required to be retrieved when this view is clicked.

        return convertView;
    }
}