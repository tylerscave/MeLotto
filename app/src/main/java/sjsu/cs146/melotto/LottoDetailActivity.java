package sjsu.cs146.melotto;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

public class LottoDetailActivity extends AppCompatActivity implements OnClickListener{

    public static final String EXTRA_NAME = "cheese_name";

    private Button saveButton;
    private Button deleteButton;
    private Button pickDateButton;
    private Spinner pickStateSpinner;
    private Spinner pickGameSpinner;
    private String[] states;
    private String[] games;
    private EditText cheeseText;
    private EditText b1;
    private EditText b2;
    private EditText b3;
    private EditText b4;
    private EditText b5;
    private EditText pb;
    private LottoTicket lottoTicket;
    private String lottoId = null;
    private byte[] bytearray;
    private int day;
    private int month;
    private int year;

    private ViewGroup vg;
    private LinearLayout mLayout;
    private LinearLayout mLayout2;
    private EditText mEditText;
    private Button dButton;
    private Button aButton;

    private int countViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lotto);


        Intent intent = getIntent();
        final String lottoName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(lottoName);

        Bundle extras = intent.getExtras();
        if(extras.getString(LottoDetailActivity.EXTRA_NAME).equals("New Lotto Ticket")){

        }
        else {
            loadBackdrop();
        }

        b1 = (EditText) findViewById(R.id.B1);
        b2 = (EditText) findViewById(R.id.B2);
        b3 = (EditText) findViewById(R.id.B3);
        b4 = (EditText) findViewById(R.id.B4);
        b5 = (EditText) findViewById(R.id.B5);
        pb = (EditText) findViewById(R.id.PB);

        saveButton = (Button) findViewById(R.id.saveButton);
        //deleteButton = (Button) findViewById(R.id.deleteButton);

        pickDateButton = (Button) findViewById(R.id.pickDateButton);

        pickStateSpinner = (Spinner) findViewById(R.id.pickStateSpinner);
        pickGameSpinner = (Spinner) findViewById(R.id.pickGameSpinner);
        states = getResources().getStringArray(R.array.lotto_state_array);
        games = getResources().getStringArray(R.array.lotto_ca_array);
        pickStateSpinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, states));
        pickStateSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        pickGameSpinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, games));
        pickGameSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());

        //lottoTicket = new LottoTicket();
        //lottoTicket.setUuidString();

        saveButton.setOnClickListener(this);
        pickDateButton.setOnClickListener(this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCam);
        fab.setOnClickListener(this);
        //vg = (ViewGroup) findViewById(R.id.linearLayout);
        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        //mLayout2 = (LinearLayout) findViewById(R.id.textFieldLayout);
        //mEditText = (EditText) findViewById(R.id.editText);
        aButton = (Button) findViewById(R.id.addButton);
        aButton.setOnClickListener(this);
        dButton = (Button) findViewById(R.id.deleteButton);
        dButton.setOnClickListener(this);
        TextView textView = new TextView(this);
        textView.setText("  ");
        countViews = 0;
    }
    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            //Toast.makeText(parent.getContext(), "Item selected is " + states[position], Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Toast.makeText(parent.getContext(), "No Item selected" , Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                ParseObject lottoTicket = new ParseObject("test");

                lottoTicket.put("B1", b1.getText().toString());
                lottoTicket.put("B2", b2.getText().toString());
                lottoTicket.put("B3", b3.getText().toString());
                lottoTicket.put("B4", b4.getText().toString());
                lottoTicket.put("B5", b5.getText().toString());
                lottoTicket.put("PB", pb.getText().toString());

                //lottoTicket.saveInBackground();
                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();
                if(imageBitmap!=null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // get byte array here
                    bytearray = stream.toByteArray();
                    // if (bytearray != null) {
                    ParseFile file = new ParseFile("picture.jpg", bytearray);
                    file.saveInBackground();
                    lottoTicket.put("profilepic", file);
                    // }
                }
                lottoTicket.put("DAY", day);
                lottoTicket.put("MONTH", month);
                lottoTicket.put("YEAR", year);
                lottoTicket.saveInBackground();
                lottoTicket.saveInBackground();
                break;
            case R.id.addButton:
                //vg.addView(createNewLayout());
                //createNewLayout();
                //mLayout2.addView(new EditText(this));
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = inflater.inflate(R.layout.test, mLayout, false);
                mLayout.addView(view2);
                countViews++;
                break;
            case R.id.deleteButton:
                if(countViews!=0){
                    mLayout.removeViewAt(mLayout.getChildCount()-1);
                    countViews--;
                }
                break;
            case R.id.fabCam:
                dispatchTakePictureIntent();
                break;
            case R.id.pickDateButton:
                alertDatePicker();
        }
    }
    /*private LinearLayout createNewLayout(){
        mLayout2.addView(createNewTextView(mEditText.getText().toString()));
        //mLayout2.addView(createNewTextView(mEditText.getText().toString()));
        return mLayout2;
    }
        private EditText createNewTextView(String text) {
            final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final EditText editText = new EditText(this);
            editText.setLayoutParams(lparams);
            editText.setLines(1);
            final int maxLength = 2;
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            editText.setText("-" + text);
            return editText;
        }*/
    //private ImageView imageView;
    private void loadBackdrop() {
        imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(picUrl).fitCenter().into(imageView);
    }

    // method and variable to set the backdrop image above
    private static String picUrl;
    public static void setPicUrl(String pic){
        picUrl = pic;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private Bitmap imageBitmap;
    private ImageView imageView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //imageView.invalidate();
            imageView = (ImageView) findViewById(R.id.backdrop);
            imageView.setImageBitmap(imageBitmap);
            //Glide.with(this).load(imageBitmap).centerCrop().into(imageView);
        }
    }
    /*
 * Show AlertDialog with date picker.
 */
    public void alertDatePicker() {

    /*
     * Inflate the XML view. activity_main is in res/layout/date_picker.xml
     */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker, null, false);

        // the time picker on the alert dialog, this is how to get the value
        final DatePicker myDatePicker = (DatePicker) view.findViewById(R.id.myDatePicker);

        // so that the calendar view won't appear
        myDatePicker.setSpinnersShown(false);
        myDatePicker.setCalendarViewShown(true);

        // the alert dialog
        new AlertDialog.Builder(this).setView(view)
                .setTitle("Set Date")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                    /*
                     * In the docs of the calendar class, January = 0, so we
                     * have to add 1 for getting correct month.
                     * http://goo.gl/9ywsj
                     */
                        month = myDatePicker.getMonth() + 1;
                        day = myDatePicker.getDayOfMonth();
                        year = myDatePicker.getYear();


                        Toast.makeText(getApplicationContext(), month + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
                        //showToast(month + "/" + day + "/" + year);
                        pickDateButton.setText(month + "/" + day + "/" + year);
                        dialog.cancel();

                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                }).show();
    }
}