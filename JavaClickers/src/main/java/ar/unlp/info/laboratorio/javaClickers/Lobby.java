package ar.unlp.info.laboratorio.javaClickers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ar.unlp.info.laboratorio.javaClickers.model.Information;
import ar.unlp.info.laboratorio.javaClickers.network.ClientManager;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;

/**
 * Created by Jony on 27/05/13.
 */
public class Lobby extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);
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
                    this.spinner = ProgressDialog.show(Lobby.this, getText(R.string.connecting_title), getText(R.string.connecting_text), true, true);
                }

                @Override
                public void taskFinished() {
                    this.spinner.dismiss();
                    ClientManager.getInstance().disconnected();
                    goToConnection();
                }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Log.i("")

        if (ClientManager.getInstance().hasInformation()){
            loadInformacion();
            if (ClientManager.getInstance().hasProblem()){
                goToProblem(false);
            }else{
                retrieveProblem();
            }
        }else{
            this.retrieveInformation();
        }
    }

    private void retrieveInformation() {
        ClientManager.getInstance().retrieveInformation(new InformationFeedback());
    }

    private class InformationFeedback implements FeedBackable{

        ProgressDialog progressDialog;

        @Override
        public void taskStarted() {
            this.progressDialog = ProgressDialog.show(Lobby.this, getText(R.string.fetching_information_title), getText(R.string.fetching_information_text));
        }

        @Override
        public void taskFinished() {
            this.progressDialog.dismiss();
            loadInformacion();
            retrieveProblem();
        }
    }

    private void loadInformacion() {
        Information information = Manager.getInstance().getInformation();
        ((TextView) this.findViewById(R.id.asignatura)).setText(information.getSubject());
        ListView temasList = (ListView) findViewById(R.id.temas_list);
        temasList.setEnabled(false);
        temasList.setAdapter(new ArrayAdapter<String>(this, R.layout.solucion_texto, information.getTopics()));
    }

    private void retrieveProblem() {
        ClientManager.getInstance().retrieveProblem(new ProblemFeedBack());
    }

    private class ProblemFeedBack implements FeedBackable{

        @Override
        public void taskStarted() {
            //TODO informar al usuario que se encuentra esperando un problema
        }

        @Override
        public void taskFinished() {
            //TODO dejar de informar que se encuentra esperando
            if (ClientManager.getInstance().hasProblem()){
                goToProblem(true);
            }
        }
    }

    private void goToProblem(boolean hasNewProblem) {
        getSharedPreferences("JC_Settings", 0).edit().putBoolean("hasNewProblem", hasNewProblem).commit();
        if (hasNewProblem){
            getSharedPreferences("JC_Settings", 0).edit().putBoolean("solutionNotSelected", hasNewProblem).commit();
        }
        startActivityIfNeeded(new Intent().setClass(this, ProblemActivity.class), 0);//.putExtra("ar.unlp.info.laboratorio.javaClickers.hasNewProblem", hasNewProblem), 0);
    }

    private void goToConnection() {
        startActivityIfNeeded(new Intent().setClass(this, MainActivity.class), 0);
    }
}
