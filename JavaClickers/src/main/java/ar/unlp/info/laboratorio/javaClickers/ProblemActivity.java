package ar.unlp.info.laboratorio.javaClickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

import ar.unlp.info.laboratorio.javaClickers.model.Solution;
import ar.unlp.info.laboratorio.javaClickers.network.ClientManager;
import ar.unlp.info.laboratorio.javaClickers.network.Manager;

/**
 * Created by Jony on 06/07/13.
 */
public class ProblemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem);

        ((ListView) findViewById(R.id.soluciones_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                confirmar(new LinkedList<Solution>(Manager.getInstance().getProblem().getSolutions()).get(position));
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.getIntent().getBooleanExtra("ar.unlp.info.laboratorio.javaClickers.hasNewProblem", false)){
            ClientManager.getInstance().startTimer(new TimerAction(){

                @Override
                public void onTick(long secondsLeft) {
                    tick(secondsLeft);
                }

                @Override
                public void onFinish() {
                    goToLobby();
                }
            });
        }
        if (Manager.getInstance().getProblem() != null){
            this.loadProblem();
        }else{
            goToLobby();
        }
    }

    private void tick(Long secondsLeft) {
        ((TextView)findViewById(R.id.tiempoRestante)).setText(secondsLeft.toString());
    }

    private void goToLobby() {
        startActivity(new Intent().setClass(this, Lobby.class));
    }

    private void loadProblem() {
        ((TextView) this.findViewById(R.id.problema)).setText(Manager.getInstance().getProblem().getAssigment());
        ((ListView) findViewById(R.id.soluciones_list)).setAdapter(new ArrayAdapter<Solution>(this, R.layout.solucion_texto, new LinkedList<Solution>(Manager.getInstance().getProblem().getSolutions())));
    }

    private void confirmar(final Solution answer){
        new AlertDialog.Builder(this)
                .setMessage("Desea confirmar la seleccion?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        disableSelection();
                        ClientManager.getInstance().newSolution(answer);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void disableSelection(){
        ((ListView) findViewById(R.id.soluciones_list)).setEnabled(false);
        ((ListView) findViewById(R.id.soluciones_list)).setBackgroundColor(Color.GRAY);
    }


}
