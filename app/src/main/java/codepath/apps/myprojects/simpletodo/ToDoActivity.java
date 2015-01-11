package codepath.apps.myprojects.simpletodo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.codepath.simpletodo.R;

/**
 * Created by i835811 on 1/10/15.
 */
public class ToDoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
