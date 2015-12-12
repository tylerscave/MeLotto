package sjsu.cs146.melotto;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class LottoPastListFragment extends Fragment {
    private static List<String> list = new ArrayList<>();
    private static List<ParseFile> pics = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_past_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                setPastTickets(LottoTicket.getPastTicketsList())));
    }

    public static List<String> setPastTickets(List<LottoTicket> tickets){
        list.clear();
        pics.clear();
        for (LottoTicket ticket : tickets){
            list.add(ticket.getNums());
            pics.add(ticket.getPic());
        }
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
            //holder.mCheckBox.setChecked(false);
            holder.mCheckBox.setChecked(LottoTicket.getPastTicketsList().get(position).getSelected());
            holder.mCheckBox.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    LottoTicket.getPastTicketsList().get(position).togglePrint();
                    //LottoTicket.getPrintTicketsList();
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