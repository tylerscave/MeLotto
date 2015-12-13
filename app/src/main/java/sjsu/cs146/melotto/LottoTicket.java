package sjsu.cs146.melotto;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LottoTicket {
    private static HashMap<String, LottoTicket> newTicketsMap = new HashMap<String, LottoTicket>();
    private static HashMap<String, LottoTicket> pastTicketsMap = new HashMap<String, LottoTicket>();
    private int[] nums;
    private int pb;
    private int date;
    private String printString;
    private ParseFile pic;
    private Boolean selected;

    public LottoTicket() {
        setNewTicketsMap();
        setPastTicketsMap();
    }


    public LottoTicket(String printString, int[] nums, int pb, int date, ParseFile pic, Boolean selected) {
        this.printString = printString;
        this.date = date;
        this.nums = nums;
        this.pb = pb;
        this.pic = pic;
        this.selected = selected;
    }

    public void togglePrint() {
        if (selected)
            this.selected = false;
        else
            this.selected = true;
    }

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



    public static int getTodaysDate() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int date = year*10000 + month*100 + day;
        return date;
    }

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
                        newTicketsMap.put(objectId, new LottoTicket(ticketString, balls, PB, ticketDate, thisPic, false));
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

    public static List<LottoTicket> getNewTicketsList() {
        List<LottoTicket> list = new LinkedList<>();
        for(LottoTicket l:newTicketsMap.values()){
            list.add(l);
        }
            return list;
    }

    public static void setPastTicketsMap(){
        int date = getTodaysDate();
        List<String> keys = Arrays.asList("B1", "B2", "B3", "B4", "B5", "PB", "DATE", "profilepic");
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


                        String ticketString = (po.getString("B1") + " " + po.getString("B2") + " " + po.getString("B3") +
                                " " + po.getString("B4") + " " + po.getString("B5") + " " + po.getString("PB")
                                + "  " + formatedDate);
                        pastTicketsMap.put(objectId, new LottoTicket(ticketString, balls, PB, ticketDate, thisPic, false));
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

    public static List<LottoTicket> getPastTicketsList() {
        List<LottoTicket> list = new LinkedList<>();
        int i=0;
        for(LottoTicket l:pastTicketsMap.values()){
            list.add(l);

            i++;
        }
        return list;
    }

    //this may be used for winning tickets or printing the pdf...
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
        return list;
    }
}