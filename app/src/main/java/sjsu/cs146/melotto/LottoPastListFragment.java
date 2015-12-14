package sjsu.cs146.melotto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

/**
 * COPYRIGHT (C) 2015 Chris Van Horn, Tyler Jones. All Rights Reserved.
 * LottoPastListFragment is responsible for fragment showing lotto tickets previous to the current
 * date
 *
 * Solves CmpE131-02 MeLotto
 * @author Chris Van Horn
 * @author Tyler Jones
 * @version 1.01 2015/12/14
 */
public class LottoPastListFragment extends Fragment {

    // declare all class variables
    private static List<String> list = new ArrayList<>();
    private static List<ParseFile> pics = new ArrayList<>();
    private static List<Boolean> winners = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_past_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    /**
     * setupRecyclerView to set up the list and refresh it as tab is selected
     * @param recyclerView
     */
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                setPastTickets(LottoTicket.getPastTicketsList())));
    }

    /**
     * setPastTickets sets the tickets from Parse
     * @param tickets
     * @return
     */
    public static List<String> setPastTickets(List<LottoTicket> tickets){
        list.clear();
        pics.clear();
        winners.clear();
        for (LottoTicket ticket : tickets){
            list.add(ticket.getPrintString());
            pics.add(ticket.getPic());
            winners.add(ticket.getWinners());
        }
        return list;
    }

    /**
     * Inner class used to adapt the list for recyclerview
     */
    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            private final CheckBox mCheckBox;
            private LottoTicket mItem;

            private ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
                mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            }
            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        private SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
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
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));
            if(winners.get(position)==true){
                holder.mTextView.setTextColor(Color.RED);
            }
            holder.mCheckBox.setChecked(LottoTicket.getPastTicketsList().get(position).getSelected());
            holder.mCheckBox.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    LottoTicket.getPastTicketsList().get(position).togglePrint();
                }
            });
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LottoDetailActivity.class);
                    intent.putExtra(LottoDetailActivity.EXTRA_NAME, holder.mBoundString);
                    context.startActivity(intent);
                    LottoDetailActivity.setPicUrl(pics.get(position).getUrl());

                }
            });
            Glide.with(holder.mImageView.getContext())
                    .load(pics.get(position).getUrl())
                    .fitCenter()
                    .into(holder.mImageView);

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}