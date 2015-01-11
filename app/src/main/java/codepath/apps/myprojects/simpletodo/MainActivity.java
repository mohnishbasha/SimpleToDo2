package codepath.apps.myprojects.simpletodo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.codepath.simpletodo.R;
import codepath.apps.myprojects.simpletodo.fragments.TodoListFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TodoListFragment())
                    .commit();
        }
    }
}