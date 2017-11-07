package me.anky.connectid.selectedConnections;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import me.anky.connectid.R;
import me.anky.connectid.data.ConnectidConnection;
import me.anky.connectid.root.ConnectidApplication;

public class SelectedConnectionsActivity extends AppCompatActivity implements
SelectedConnectionsActivityMVP.View{
    private String idsString;
    private List<String> idsList;

    @Inject
    SelectedConnectionsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_connections);

        ((ConnectidApplication) getApplication()).getApplicationComponent().inject(this);

        Intent intent = getIntent();
        idsString = intent.getStringExtra("ids");
        idsList = new ArrayList(Arrays.asList(idsString.split(", ")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.loadConnections(idsList);
    }


    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void displayConnections(List<ConnectidConnection> connections) {
        int size = connections.size();
        Toast.makeText(this, "size " + size, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayNoConnections() {

    }

    @Override
    public void displayError() {

    }
}
