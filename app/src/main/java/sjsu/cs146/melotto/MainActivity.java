package sjsu.cs146.melotto;

import android.app.Application;
import com.parse.Parse;

/**
 * COPYRIGHT (C) 2015 Chris Van Horn, Tyler Jones. All Rights Reserved.
 * MainActivity class is the entry point for the MeLotto app. It is responsible for initializing
 * the Parse SDK
 *
 * Solves CmpE131-02 MeLotto
 * @author Chris Van Horn
 * @author Tyler Jones
 * @version 1.01 2015/12/14
 */
public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }
}
