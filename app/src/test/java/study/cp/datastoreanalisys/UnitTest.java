package study.cp.datastoreanalisys;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void parseResult() throws Exception {
        String stmt = "CREATE TABLE";
        String result = "asdasd c asdasd 3; CREATE TABLE title(_id INTEGER PRIMARY KEY AUTOINCREMENT, thread_id INTEGER);CREATE TABLE title2(_id INTEGER PRIMARY KEY AUTOINCREMENT, thread_id INTEGER);";
        while (result.contains(stmt)){
            int index = result.indexOf(stmt);
            result=result.substring(index);
            int next = result.indexOf("(");
            String table_name = result.substring(stmt.length()+1,next);
            System.out.println(table_name);
//            assertEquals(table_name,"title");

            String[] toParse = result.substring(next+1,result.indexOf(")")).split(",");
            for (String item:toParse){
                String[] keywords = item.trim().split(" ");
                System.out.println(keywords[0]);
                System.out.println(keywords[1]);
                System.out.println(Arrays.toString(keywords));
            }
            result = result.substring(result.indexOf(")"));
        }

    }
}