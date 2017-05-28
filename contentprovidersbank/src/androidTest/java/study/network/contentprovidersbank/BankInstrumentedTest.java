package study.network.contentprovidersbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BankInstrumentedTest {

    private ContentValues getTestData(){
        ContentValues values = new ContentValues();
        values.put(UsersRelationProvider.NAME, "test name");
        values.put(UsersRelationProvider.PASSWORD,"test password");
        return values;
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("study.network.contentprovidersbank", appContext.getPackageName());
    }

    @Test
    public void useSQLProvider() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Uri uri = appContext.getContentResolver().insert(
                UsersRelationProvider.CONTENT_URI, getTestData());

        assertEquals("study.network.contentprovidersbank", appContext.getPackageName());
    }
}
