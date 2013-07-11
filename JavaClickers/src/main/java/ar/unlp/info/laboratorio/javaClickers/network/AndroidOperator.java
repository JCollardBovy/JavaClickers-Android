package ar.unlp.info.laboratorio.javaClickers.network;

import android.os.AsyncTask;
import android.os.CountDownTimer;

import ar.unlp.info.laboratorio.javaClickers.FeedBackable;
import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;

/**
 * Created by Jony on 24/05/13.
 */
public class AndroidOperator extends Operator {

    //AsyncTask<Object, Object, Boolean> currentTask = null;

    @Override
    public void executeTask(Operation operation, FeedBackable aFeedBackInterface) {
//        currentTask = new ExecuteTask().execute(operation, aFeedBackInterface);
        new ExecuteTask().execute(operation, aFeedBackInterface);
    }

    @Override
    public void startTimer(long secondsLeft, final TimerAction timerAction) {
        new CountDownTimer(secondsLeft*1000, 1000){
            @Override
            public void onTick(long l) {
                timerAction.onTick(l/1000);
            }

            @Override
            public void onFinish() {
                timerAction.onTick(0);
                ClientManager.getInstance().setProblem(null);
                timerAction.onFinish();
            }
        }.start();
    }

    private class ExecuteTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object... objects) {
            ((Operation) objects[0]).executeOnClient();
            this.publishProgress(objects);
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            ((FeedBackable) values[1]).taskFinished();
        }
    }


}
