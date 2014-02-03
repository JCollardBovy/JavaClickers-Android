package ar.unlp.info.laboratorio.javaClickers.network;

import android.util.Log;

import java.net.InetSocketAddress;

import ar.unlp.info.laboratorio.javaClickers.FeedBackable;
import ar.unlp.info.laboratorio.javaClickers.TimerAction;
import ar.unlp.info.laboratorio.javaClickers.model.Solution;
import ar.unlp.info.laboratorio.javaClickers.network.operations.AnswerOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.ConnectOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.DisconnectOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.InformationOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.Operation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.ProblemOperation;

/**
 * Created by Jony on 03/07/13.
 */
public class ClientManager extends Manager{

    /**
     * Estado de la conexion actual al servidor.
     */
    private ConnectionState estado;

    /**
     * Metodo Singleton que retorna la instancia de ClientManager
     * @return Retorna una instancia de ClientManager
     */
    public static synchronized ClientManager getInstance() {
        if(instance == null){
            instance = new ClientManager();
        }
        return (ClientManager) instance;
    }

    /**
     * Invalida el constructor para no generar nuevas instancias.
     */
    private ClientManager() {
        super();
        this.setOperator(new AndroidOperator());
        this.setEstado(new DisconnectedState());
    }

    @Override
    public void setServer(InetSocketAddress server) {
        super.setServer(server);
        this.setEstado(new IdleState());
    }

    public void retrieveInformation(FeedBackable informationFeedback) {
            this.getOperator().execute(new InformationOperation(), informationFeedback);
    }

    /**
     *
     * @param aFeedBackInterface
     */
    public void connect(FeedBackable aFeedBackInterface) {
        this.getEstado().connect(aFeedBackInterface);
    }

    protected ConnectionState getEstado() {
        return estado;
    }

    protected void setEstado(ConnectionState estado) {
        this.estado = estado;
        Log.i("[JC]", "State: " + estado.toString());
    }

    public void connected() {
        this.setEstado(new IdleState());
    }

    public void retrieveProblem(FeedBackable problemFeedBack) {
        this.getEstado().execute(new ProblemOperation(), problemFeedBack);
    }

    public boolean hasInformation() {
        return (this.getInformation()!= null);
    }

    public boolean hasProblem() {
        return (this.getProblem()!= null);
    }

    public void newSolution(Solution aSolution){
        this.getOperator().execute(new AnswerOperation(aSolution), new FeedBackable() {
            @Override
            public void taskStarted() {
            }

            @Override
            public void taskFinished() {
            }
        });
    }

    @Override
    public void disconnect(Object aDisconnectParam) {
        this.getEstado().disconnect((FeedBackable)aDisconnectParam);
    }

    public void bindTimer(TimerAction aTimerAction){
        this.getOperator().bindTimer(aTimerAction);
    }

    /**
     * Created by Jony on 23/05/13.
     */
    public void disconnected() {
        super.setServer(null);
        this.setInformation(null);
        this.setEstado(new DisconnectedState());
    }

    public void finished() {
        this.setEstado(new IdleState());
    }

    /**
     * Created by Jony on 23/05/13.
     */
    public abstract class ConnectionState {

        public void connect(FeedBackable aFeedBackInterface){
            aFeedBackInterface.taskFinished();
        }

        public void disconnect(FeedBackable aFeedBackFeedBackable) {
            getOperator().execute(new DisconnectOperation(getServer().getPort()), aFeedBackFeedBackable);
        }

        public void execute(Operation aOperation, FeedBackable aFeedBackFeedBackable) {
            aFeedBackFeedBackable.taskFinished();
        }
    }

    public class DisconnectedState extends ConnectionState {
        @Override
        public void connect(FeedBackable aFeedBackInterface) {
            getOperator().execute(new ConnectOperation(), aFeedBackInterface);
        }

        @Override
        public void disconnect(FeedBackable aFeedBackFeedBackable){
            aFeedBackFeedBackable.taskFinished();
        }
    }

    private class IdleState extends ConnectionState {

        @Override
        public void execute(Operation aOperation, FeedBackable aFeedBackFeedBackable) {
            setEstado(new BusyState());
            getOperator().execute(aOperation, aFeedBackFeedBackable);
        }
    }

    private class BusyState extends ConnectionState{
    }
}
