package ar.unlp.info.laboratorio.javaClickers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ar.unlp.info.laboratorio.javaClickers.network.ClientManager;

/**
 *
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        discoverButton(((Button) findViewById(R.id.discoverButton)));

        Log.i("[JC]", "Start");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        ClientManager.getInstance().disconnect(new FeedBackable() {

            ProgressDialog spinner;

            @Override
            public void taskStarted() {
                this.spinner = ProgressDialog.show(MainActivity.this, getText(R.string.connecting_title), getText(R.string.connecting_text), true, true);
            }

            @Override
            public void taskFinished() {
                this.spinner.dismiss();
                Log.i("[JC]", "Disconnected");
                ClientManager.getInstance().disconnected();
            }

        });
        return true;
    }

    /**
     * Establece el comportamiento del boton que realiza la busqueda del servidor
     * @param discover
     */
    private void discoverButton(Button discover) {
        discover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ClientManager.getInstance().getServer() != null) {
                    Log.i("[JC]", "Server != null");
                    goToLobby();
                }else{
                    Log.i("[JC]", "Discover");
                    ClientManager.getInstance().connect(new ConnectionFeedBack());
                }
            }
        });
    }

    private void goToLobby(){
        Log.i("[JC]", "GoToLobby");
        startActivityIfNeeded(new Intent().setClass(this, Lobby.class), 0);
    }

    private class ConnectionFeedBack implements FeedBackable {

        ProgressDialog progressDialog;

        @Override
        public void taskStarted() {
            Log.i("[JC]", "Discovering");
            this.progressDialog = ProgressDialog.show(MainActivity.this, getText(R.string.connecting_title), getText(R.string.connecting_text), true, true);
        }

        @Override
        public void taskFinished() {
            this.progressDialog.dismiss();
            Log.i("[JC]", "Connected");
            goToLobby();
            Toast.makeText(MainActivity.this, R.string.connected, Toast.LENGTH_SHORT).show();
        }
    }

}
