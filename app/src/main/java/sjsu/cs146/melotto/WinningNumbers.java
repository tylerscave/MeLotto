package sjsu.cs146.melotto;

import android.util.Log;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * COPYRIGHT (C) 2015 Chris Van Horn. All Rights Reserved.
 * WinningNumbers class is responsible for determining winning lottery tickets
 *
 * Solves CmpE131-02 MeLotto
 * @author Chris Van Horn
 * @version 1.01 2015/12/14
 */
public class WinningNumbers {

    // declare all class variables
    private static LinkedList<WinningNumbers> list = new LinkedList<>();
    private int drawDate;
    private int[] Ball = new int[5];
    private int PB;

    /**
     * constructor to set winning numbers when object is created
     */
    public WinningNumbers(){
        setWinningNumbers();
    }

    /**
     * constructor for the winning numbers object
     * @param drawDate
     * @param Ball
     * @param PB
     */
    public WinningNumbers(int drawDate, int[] Ball, int PB){
        this.drawDate = drawDate;
        this.Ball = Ball;
        this.PB = PB;
    }

    // accessors and mutators for the class
    public static LinkedList<WinningNumbers> getWinningNumbers(){
        return list;
    }

    public int getDrawDate(){
        return drawDate;
    }

    public int[] getBalls(){
        return Ball;
    }

    public int getPB() {
        return PB;
    }

    /**
     * setWinningNumbers() is responsible for getting winning tickets from Parse
     */
    public static void setWinningNumbers(){
        List<String> keys = Arrays.asList("DATE", "WB1", "WB2", "WB3", "WB4", "WB5", "PB");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("lotto");
        query.selectKeys(keys);
        //query.orderByDescending("DATE");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> query, ParseException e) {
                if (e == null) {
                    for (ParseObject po : query) {
                        int dd = po.getInt("DATE");
                        int[] b = new int[5];
                        b[0]=po.getInt("WB1");
                        b[1]=po.getInt("WB2");
                        b[2]=po.getInt("WB3");
                        b[3]=po.getInt("WB4");
                        b[4]=po.getInt("WB5");
                        int pb=po.getInt("PB");
                        list.add(new WinningNumbers(dd, b, pb));
                    }
                }else{
                    Log.d("DATE", "Error: " + e.getMessage());
                    Log.d("WB1", "Error: " + e.getMessage());
                    Log.d("WB2", "Error: " + e.getMessage());
                    Log.d("WB3", "Error: " + e.getMessage());
                    Log.d("WB4", "Error: " + e.getMessage());
                    Log.d("WB5", "Error: " + e.getMessage());
                    Log.d("PB", "Error: " + e.getMessage());
                }
            }
        });
    }
}
