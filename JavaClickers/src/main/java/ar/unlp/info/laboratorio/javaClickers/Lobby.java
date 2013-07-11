package ar.unlp.info.laboratorio.javaClickers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
    protected void onResume() {
        super.onResume();

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
        ((TextView) this.findViewById(R.id.asignatura)).setText(information.getAsignatura());
        ListView temasList = (ListView) findViewById(R.id.temas_list);
        temasList.setEnabled(false);
        temasList.setAdapter(new ArrayAdapter<String>(this, R.layout.solucion_texto, information.getTemas()));
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
            goToProblem(true);
        }
    }

    private void goToProblem(boolean hasNewProblem) {
        startActivity(new Intent().setClass(this, ProblemActivity.class).putExtra("ar.unlp.info.laboratorio.javaClickers.hasNewProblem", hasNewProblem));
    }
}
