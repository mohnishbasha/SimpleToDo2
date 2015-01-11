package codepath.apps.myprojects.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.simpletodo.R;

/**
 * Created by i835811 on 1/10/15.
 */
public class EditItemActivity extends Activity {

    private int editItemPos = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String editItemText = getIntent().getStringExtra("editItemText");
        editItemPos = getIntent().getIntExtra("editItemPos", 0);
        EditText etItem = (EditText) findViewById(R.id.mlEditItem);
        etItem.setText(editItemText);
        etItem.setSelection(etItem.getText().length());
        etItem.setFocusable(true);

        //Toast.makeText(this, editItemText + "-" + editItemPos, Toast.LENGTH_SHORT).show();
    }

    public void onSave(View v) {

        EditText etItem = (EditText) findViewById(R.id.mlEditItem);
        Intent data = new Intent();
        data.putExtra("editedText", etItem.getText().toString());
        data.putExtra("editedItemPos", editItemPos);
        data.putExtra("action", "save");
        setResult(RESULT_OK, data); // set result code and bundle data for response
        // closes the activity and returns to first screen
        this.finish();
    }

    public void onDelete(View v) {
        Intent data = new Intent();
        data.putExtra("editedItemPos", editItemPos);
        data.putExtra("action", "delete");
        setResult(RESULT_OK, data); // set result code and bundle data for response
        // closes the activity and returns to first screen
        this.finish();
    }
}
