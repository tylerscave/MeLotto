package sjsu.cs146.melotto;

import android.util.Log;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * COPYRIGHT (C) 2015 Chris Van Horn, Tyler Jones. All Rights Reserved.
 * LottoTicket class defines the lotto ticket object and is responsible for pulling user tickets
 * from Parse
 *
 * Solves CmpE131-02 MeLotto
 * @author Chris Van Horn
 * @author Tyler Jones
 * @version 1.01 2015/12/14
 */
public class LottoTicket implements Comparable<LottoTicket>{

    // declare all class variables
    private static HashMap<String, LottoTicket> newTicketsMap = new HashMap<String, LottoTicket>();
    private static HashMap<String, LottoTicket> pastTicketsMap = new HashMap<String, LottoTicket>();
    private int[] nums;
    private int pb;
    private int date;
    private String printString;
    private ParseFile pic;
    private Boolean selected;
    private Boolean winner;

    /**
     * constructor to pass tickets to fragments as object is created
     */
    public LottoTicket() {
        setNewTicketsMap();
        setPastTicketsMap();
    }

    /**
     * constructor to set up the lotto ticket object
     * @param printString
     * @param nums
     * @param pb
     * @param date
     * @param pic
     * @param selected
     * @param winner
     */
    public LottoTicket(String printString, int[] nums, int pb, int date, ParseFile pic, Boolean selected, Boolean winner) {
        this.printString = printString;
        this.date = date;
        this.nums = nums;
        this.pb = pb;
        this.pic = pic;
        this.selected = selected;
        this.winner = winner;
    }

    /**
     * togglePrint() handles behavior when check boxes are checked for each ticket
     * this is used for passing tickets to the printlist fragment
     */
    public void togglePrint() {
        if (selected)
            this.selected = false;
        else
            this.selected = true;
    }

    // accessors and mutators for variables in this class
    public Boolean getSelected() {
        return selected;
    }

    public int[] getNums() {
        return nums;
    }

    public int getDate() {
        return date;
    }

    public int getPb() {
        return pb;
    }

    public String getPrintString() {
        return printString;
    }

    public ParseFile getPic() {
        return pic;
    }

    public Boolean getWinners(){
        return winner;
    }

    /**
     * getTodaysDate()
     * @return the current date
     */
    public static int getTodaysDate() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int date = year*10000 + month*100 + day;
        return date;
    }

    /**
     * setNewTicketsMap() gets all info for future tickets from Parse
     */
    public static void setNewTicketsMap(){
        int date = getTodaysDate();
        List<String> keys = Arrays.asList("B1", "B2", "B3", "B4", "B5", "PB", "DATE", "profilepic");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("test");
        query.selectKeys(keys);
        query.whereGreaterThanOrEqualTo("DATE", date);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> query, ParseException e) {
                if (e == null) {
                    for (ParseObject po : query) {
                        ParseFile thisPic = po.getParseFile("profilepic");
                        Integer ticketDate = po.getInt("DATE");
                        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
                        Date aDate = null;
                        try {
                            aDate = originalFormat.parse(ticketDate.toString());
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                        SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String formatedDate = newFormat.format(aDate);
                        String objectId = po.getObjectId();
                        int[] balls = new int[5];
                        balls[0]=Integer.parseInt(po.getString("B1"));
                        balls[1]=Integer.parseInt(po.getString("B2"));
                        balls[2]=Integer.parseInt(po.getString("B3"));
                        balls[3]=Integer.parseInt(po.getString("B4"));
                        balls[4]=Integer.parseInt(po.getString("B5"));
                        int PB=Integer.parseInt(po.getString("PB"));

                        String ticketString = (po.getString("B1") + " " + po.getString("B2") + " " + po.getString("B3") +
                                " " + po.getString("B4") + " " + po.getString("B5") + " " + po.getString("PB")
                                + "  " + formatedDate);
                        newTicketsMap.put(objectId, new LottoTicket(ticketString, balls, PB, ticketDate, thisPic, false, false));
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

    /**
     * getNewTicketsList() accessor for getting new tickets
     * @return
     */
    public static List<LottoTicket> getNewTicketsList() {
        ArrayList<LottoTicket> list = new ArrayList<>();
        for(LottoTicket l:newTicketsMap.values()){
            list.add(l);
        }
        Collections.sort(list);
            return list;
    }

    @Override
    public int compareTo(LottoTicket stuff) {
        int compareDates = stuff.getDate();
        return compareDates - this.date;
    }
    @Override
    public String toString() {
        return "";
    }

    /**
     * setPastTicketsMap() gets all info for past tickets from Parse
     */
    public static void setPastTicketsMap(){
        int date = getTodaysDate();
        List<String> keys = Arrays.asList("B1", "B2", "B3", "B4", "B5", "PB", "DATE", "profilepic", "WINNER");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("test");
        query.selectKeys(keys);
        query.whereLessThan("DATE", date);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> query, ParseException e) {
                if (e == null) {
                    for (ParseObject po : query) {
                        ParseFile thisPic = po.getParseFile("profilepic");
                        Integer ticketDate = po.getInt("DATE");
                        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
                        Date aDate = null;
                        try {
                            aDate = originalFormat.parse(ticketDate.toString());
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                        SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String formatedDate = newFormat.format(aDate);
                        String objectId = po.getObjectId();
                        int[] balls = new int[5];
                        balls[0]=Integer.parseInt(po.getString("B1"));
                        balls[1]=Integer.parseInt(po.getString("B2"));
                        balls[2]=Integer.parseInt(po.getString("B3"));
                        balls[3]=Integer.parseInt(po.getString("B4"));
                        balls[4]=Integer.parseInt(po.getString("B5"));
                        int PB=Integer.parseInt(po.getString("PB"));

                        for(WinningNumbers w:WinningNumbers.getWinningNumbers()) {
                            if(ticketDate==w.getDrawDate()) {
                                if(compare(balls, w.getBalls()) && PB==w.getPB()) {
                                    po.put("WINNER",true);
                                    po.saveInBackground();
                                }
                                else{}
                            }
                        }
                        Boolean wn = po.getBoolean("WINNER");

                        String ticketString = (po.getString("B1") + " " + po.getString("B2") + " " + po.getString("B3") +
                                " " + po.getString("B4") + " " + po.getString("B5") + " " + po.getString("PB")
                                + "  " + formatedDate);
                        pastTicketsMap.put(objectId, new LottoTicket(ticketString, balls, PB, ticketDate, thisPic, false, wn));
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

    public static boolean compare(int[] a, int[] b){
        int matches=0;
        for(int i = 0; i < a.length-1; i++){
            for(int j = 0; j < b.length; j++){
                if(a[i]==b[j])
                    matches++;
            }
        }
        if(matches==4)
            return true;
        else
            return false;
    }

    /**
     * getPastTicketsList() accessor for getting past tickets
     * @return
     */
    public static List<LottoTicket> getPastTicketsList() {
        List<LottoTicket> list = new LinkedList<>();
        int i=0;
        for(LottoTicket l:pastTicketsMap.values()){
            list.add(l);
            i++;
        }
        Collections.sort(list);
        return list;
    }

    /**
     * getPrintTicketsList() used for winning tickets or printing the pdf...
     * @return
     */
    public static List<LottoTicket> getPrintTicketsList() {
        List<LottoTicket> list = new LinkedList<>();
        for(LottoTicket l:newTicketsMap.values()){
            if(l.getSelected())
                list.add(l);
            else{}
        }
        for(LottoTicket l:pastTicketsMap.values()){
            if(l.getSelected())
                list.add(l);
            else{}
        }
        Collections.sort(list);
        return list;
    }
}