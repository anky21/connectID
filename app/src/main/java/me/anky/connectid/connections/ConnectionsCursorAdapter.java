package me.anky.connectid.connections;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.anky.connectid.R;
import me.anky.connectid.data.source.local.IConnectidColumns;
import me.anky.connectid.details.DetailsActivity;

public class ConnectionsCursorAdapter
        extends CursorRecyclerViewAdapter<ConnectionsCursorAdapter.ViewHolder> {

    Context mContext;
    ViewHolder mVh;

    public ConnectionsCursorAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView mListItemTv;
        public ViewHolder(View view){
            super(view);
            mListItemTv = (TextView) view.findViewById(R.id.list_item_tv);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.i("ONCLICK_TEST", "Clicked database item id: " + mListItemTv.getTag());
            Log.i("ONCLICK_TEST", mListItemTv.getText().toString());

            // TODO Just testing. Can be moved. DetailsActivity can be replaced.
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("ID", (int) mListItemTv.getTag());
            intent.putExtra("DETAILS", mListItemTv.getText().toString());
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connections_list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
        int id = cursor.getInt(cursor.getColumnIndex(IConnectidColumns._ID));
        String name = cursor.getString(cursor.getColumnIndex(IConnectidColumns.NAME));
        String description = cursor.getString(cursor.getColumnIndex(IConnectidColumns.DESCRIPTION));

        viewHolder.mListItemTv.setTag(id);
        viewHolder.mListItemTv.setText(name + " - " + description);
    }

}