package me.anky.connectid.connections;

import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.anky.connectid.R;
import me.anky.connectid.data.source.local.IConnectidColumns;
import me.anky.connectid.data.ConnectidConnection;
import me.anky.connectid.data.source.local.ConnectidProvider;
import me.anky.connectid.details.DetailsActivity;

public class ConnectionsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ConnectionsActivityView {

    private static final int CURSOR_LOADER_ID = 0;
    private ConnectionsCursorAdapter mCursorAdapter;

    @BindView(R.id.connections_list_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private Object mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        Stetho.initializeWithDefaults(this);

        mPresenter = new ConnectionsActivityPresenter(this, null);

        testSchematic();


        mCursorAdapter = new ConnectionsCursorAdapter(this, null);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCursorAdapter);
        // Automatically hide/show the FAB when recycler view scrolls up/down
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    mFab.hide();
                } else{
                    mFab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void displayConnections(List<ConnectidConnection> connections) {

    }

    @Override
    public void displayNoConnections() {

    }







    private void testSchematic() {

        // Clear database
//        getContentResolver().delete(
//                ConnectidProvider.Connections.CONTENT_URI,
//                null,
//                null);

        Cursor cursor = getContentResolver().query(
                ConnectidProvider.Connections.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor == null || cursor.getCount() == 0) {
            insertTestData();
        }
    }

    // OnclickListener for the FAB
    @OnClick(R.id.fab)
    public void fabOnClick(){
        startActivity(new Intent(this, DetailsActivity.class));
    }

    public void insertTestData() {

        ConnectidConnection[] connections = {
                new ConnectidConnection("Aragorn", "you have my sword"),
                new ConnectidConnection("Legolas", "and you have my bow"),
                new ConnectidConnection("Gimli", "and my axe!"),
                new ConnectidConnection("Aragorn", "elf lover"),
                new ConnectidConnection("Gandalf", "great with fireworks"),
                new ConnectidConnection("Bilbo", "misses his ring"),
                new ConnectidConnection("Frodo", "misses his finger"),
                new ConnectidConnection("Ezio", "skilled assassin"),
                new ConnectidConnection("Tony Stark", "he is Iron Man"),
                new ConnectidConnection("Captain America", "boring"),
                new ConnectidConnection("Luke Skywalker", "in denial"),
                new ConnectidConnection("Darth Vader", "loving father"),
                new ConnectidConnection("Xenomorph", "saliva is acid"),
                new ConnectidConnection("GLaDOS", "not even angry"),
                new ConnectidConnection("Yoda", "met in swamp"),
                new ConnectidConnection("Shrek", "met in swamp"),
                new ConnectidConnection("Donkey", "very talkative"),
                new ConnectidConnection("Bowser", "has evil laugh"),
                new ConnectidConnection("Princess Peach", "damsel in distress"),
                new ConnectidConnection("Princess Peach", "damsel in distress"),
                new ConnectidConnection("Princess Peach", "damsel in distress"),
                new ConnectidConnection("Princess Peach", "damsel in distress"),
                new ConnectidConnection("Princess Peach", "damsel in distress"),
                new ConnectidConnection("Princess Zelda", "damsel in distress"),
                new ConnectidConnection("Mario", "jumps a lot")
        };

        ArrayList<ContentProviderOperation> batchOperations =
                new ArrayList<>(connections.length);

        for (ConnectidConnection connection : connections) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    ConnectidProvider.Connections.CONTENT_URI);
            builder.withValue(IConnectidColumns.NAME, connection.getName());
            builder.withValue(IConnectidColumns.DESCRIPTION, connection.getDescription());
            batchOperations.add(builder.build());
        }

        try {
            getContentResolver().applyBatch(ConnectidProvider.AUTHORITY, batchOperations);

        } catch (RemoteException | OperationApplicationException e) {
            Log.e("DATABASE_TEST", "Error applying batch insert", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(this, ConnectidProvider.Connections.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mCursorAdapter.swapCursor(null);
    }


}
