package study.network.contentprovidersbank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentValues values = new ContentValues();
        values.put("name", "alex");
        values.put("password", "aasdasd");

        Uri uri = getContentResolver().insert(
                Uri.parse("content://""com.example.doc.users"), values);
        Cursor cursor = managedQuery(Uri.parse("content://""com.example.doc.users"), null, null, null, null);
        Toast.makeText(this, String.valueOf(cursor.getColumnCount()), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, cursor.getString(0), Toast.LENGTH_SHORT).show();

    }

    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(UsersRelationProvider.NAME,
                ((EditText) findViewById(R.id.editText2)).getText().toString());

        values.put(UsersRelationProvider.PASSWORD,
                ((EditText) findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                UsersRelationProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = UsersRelationProvider.URL;

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(UsersRelationProvider._ID))
                        ", "c.getString(c.getColumnIndex(UsersRelationProvider.NAME))
                        ", "c.getString(c.getColumnIndex(UsersRelationProvider.PASSWORD)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }
