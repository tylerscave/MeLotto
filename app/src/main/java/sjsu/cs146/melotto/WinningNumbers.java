package sjsu.cs146.melotto;

import android.util.Log;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.Arrays;
import java.util.List;

public class WinningNumbers {

    private static int drawDate;
    private static int[] B;
    private static int PB;

    public static void setNewTicketsMap(){
        List<String> keys = Arrays.asList("DATE", "B1", "B2", "B3", "B4", "B5", "PB");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("lotto");
        query.selectKeys(keys);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> query, ParseException e) {
                if (e == null) {
                    for (ParseObject po : query) {
                        drawDate = po.getInt("DATE");

                        String objectId = po.getObjectId();
                        //String objectId = po.getString("objectId");
                        String thisTicket = (po.getString("B1") + " " + po.getString("B2") + " " + po.getString("B3") +
                                " " + po.getString("B4") + " " + po.getString("B5") + " " + po.getString("PB")
                                + "  ");
                    }
                }else{
                    Log.d("B1", "Error: " + e.getMessage());
                    Log.d("B2", "Error: " + e.getMessage());
                    Log.d("B3", "Error: " + e.getMessage());
                    Log.d("B4", "Error: " + e.getMessage());
                    Log.d("B5", "Error: " + e.getMessage());
                    Log.d("PB", "Error: " + e.getMessage());
                    Log.d("DATE", "Error: " + e.getMessage());
                    Log.d("profilepic", "Error: " + e.getMessage());
                }
            }
        });
    }
}
