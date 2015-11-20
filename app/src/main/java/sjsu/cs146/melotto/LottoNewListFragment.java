package sjsu.cs146.melotto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


public class LottoNewListFragment extends Fragment {
    private List<String> list = new ArrayList<>();
    private static List<ParseFile> pics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_new_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                getNewList()));
    }

    private List<String> getNewList() {
        List<String> keys = Arrays.asList("B1", "B2", "B3", "B4", "B5", "PB", "MONTH", "DAY", "YEAR", "profilepic");
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("test");
        query.whereGreaterThanOrEqualTo("YEAR", year);
        query.whereGreaterThanOrEqualTo("MONTH", month);
        query.whereGreaterThanOrEqualTo("DAY", day);
        query.selectKeys(keys);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> query, ParseException e) {
                if (e == null) {
                    for (ParseObject po : query) {
                        pics.add(po.getParseFile("profilepic"));
                        list.add(po.getString("B1") + " " + po.getString("B2") + " " + po.getString("B3") +
                                " " + po.getString("B4") + " " + po.getString("B5") + " " + po.getString("PB") +
                                " date " + po.getInt("MONTH") + "/" + po.getInt("DAY") + "/" + po.getInt("YEAR"));
                    }
                } else {
                    Log.d("B1", "Error: " + e.getMessage());
                    Log.d("B2", "Error: " + e.getMessage());
                    Log.d("B3", "Error: " + e.getMessage());
                    Log.d("B4", "Error: " + e.getMessage());
                    Log.d("B5", "Error: " + e.getMessage());
                    Log.d("PB", "Error: " + e.getMessage());
                    Log.d("MONTH", "Error: " + e.getMessage());
                    Log.d("DAY", "Error: " + e.getMessage());
                    Log.d("YEAR", "Error: " + e.getMessage());
                }
            }
        });
        HashSet<String> hs = new HashSet<>();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            //public final ParseImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                //mImageView = (ParseImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LottoDetailActivity.class);
                    intent.putExtra(LottoDetailActivity.EXTRA_NAME, holder.mBoundString);
                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
             .load(pics.get(0).getUrl())
                 .fitCenter()
                 .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}