package sjsu.cs146.melotto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.parse.ParseUser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LottoActivity extends AppCompatActivity {

    //private DrawerLayout mDrawerLayout;
    //private int backButtonCount;
    private ViewPager viewPager;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto);

        // instantiate LottoTicket to query parse before fragments load
        new LottoTicket();

        // adding a delay to allow parse query to complete before app continues
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.fab:
                        if(viewPager.getCurrentItem()<2){
                            //Toast.makeText(getApplicationContext(),"First",Toast.LENGTH_LONG).show();
                            Context context = view.getContext();
                            Intent intent = new Intent(context, LottoDetailActivity.class);
                            intent.putExtra(LottoDetailActivity.EXTRA_NAME, "New Lotto Ticket");
                            context.startActivity(intent);
                        }
                        else if(viewPager.getCurrentItem() == 2){
                            Toast.makeText(getApplicationContext(),"MeLotto.pdf was created for you",
                                    Toast.LENGTH_LONG).show();
                            try {
                                makePdf();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (DocumentException de) {
                                de.printStackTrace();
                            }
                        }
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2) {
                    fab.setImageResource(R.drawable.ic_pdf);
                }
                else
                    fab.setImageResource(R.drawable.ic_add);
                /*if(position==1) {
                    Toast.makeText(LottoActivity.this,
                            "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                    LottoTicket.getPastTicketsList();
                    adapter.notifyDataSetChanged();
                    //ViewGroup vg = (ViewGroup) findViewById (R.id.recyclerview);
                    //vg.invalidate();
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * makePdf is used to create and save to disk a PDF of selected tickets
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    private void makePdf() throws FileNotFoundException, DocumentException {
        List<LottoTicket> printList = LottoTicket.getPrintTicketsList();

        // Create the new PDF doc to be written to
        Document document = new Document();
        String file = Environment.getExternalStorageDirectory().getPath() + "/MeLotto.pdf";
        PdfWriter.getInstance(document,new FileOutputStream(file));
        document.newPage();
        document.open();

        // add new tickets and a title to the PDF
        document.add(new Paragraph("MeLotto Ticket Numbers"));
        for (LottoTicket lt : printList){
            document.add(new Paragraph(lt.getPrintString()));
        }
        document.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                //something
                return true;
            case R.id.logoff:
                ParseUser.logOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new LottoNewListFragment(), "New Tickets");
        adapter.addFragment(new LottoPastListFragment(), "Past Tickets");
        adapter.addFragment(new LottoPrintListFragment(), "Print Report");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }


    }
}