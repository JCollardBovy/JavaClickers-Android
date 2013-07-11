package ar.unlp.info.laboratorio.javaClickers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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
    }

    /**
     * Establece el comportamiento del boton que realiza la busqueda del servidor
     * @param discover
     */
    private void discoverButton(Button discover) {
        discover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClientManager.getInstance().connect(new ConnectionFeedBack());
            }
        });
    }

    private void goToLobby(){
        startActivity(new Intent().setClass(this, Lobby.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class ConnectionFeedBack implements FeedBackable {

        ProgressDialog progressDialog;

        @Override
        public void taskStarted() {
            this.progressDialog = ProgressDialog.show(MainActivity.this, getText(R.string.connecting_title), getText(R.string.connecting_text), true, true);
        }

        @Override
        public void taskFinished() {
            this.progressDialog.dismiss();
            goToLobby();
            //TODO Agregar TOSS (Informe al usuario)
        }
    }

}
