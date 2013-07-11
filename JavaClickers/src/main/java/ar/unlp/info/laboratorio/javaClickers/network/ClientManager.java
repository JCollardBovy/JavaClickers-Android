package ar.unlp.info.laboratorio.javaClickers.network;

import java.net.InetSocketAddress;

import ar.unlp.info.laboratorio.javaClickers.FeedBackable;
import ar.unlp.info.laboratorio.javaClickers.model.Solution;
import ar.unlp.info.laboratorio.javaClickers.network.operations.AnswerOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.ConnectOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.InformationOperation;
import ar.unlp.info.laboratorio.javaClickers.network.operations.ProblemOperation;

/**
 * Created by Jony on 03/07/13.
 */
public class ClientManager extends Manager{

    /**
     * Estado de la conexion actual al servidor.
     */
    private EstadoConexion estado;

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
        this.setEstado(new EstadoDesconectado());
    }

    @Override
    public void setServer(InetSocketAddress server) {
        super.setServer(server);
        this.setEstado(new EstadoConectado());
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

    protected EstadoConexion getEstado() {
        return estado;
    }

    protected void setEstado(EstadoConexion estado) {
        this.estado = estado;
    }

    public void connected() {
        this.setEstado(new EstadoConectado());
    }

    public void retrieveProblem(FeedBackable problemFeedBack) {
            this.getOperator().execute(new ProblemOperation(), problemFeedBack);
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

    /**
     * Created by Jony on 23/05/13.
     */
    public abstract static class EstadoConexion {

        public void connect(FeedBackable aFeedBackInterface){
            aFeedBackInterface.taskFinished();
        }

    }

    /**
     * Created by Jony on 23/05/13.
     */
    public class EstadoDesconectado extends EstadoConexion {

        @Override
        public void connect(FeedBackable aFeedBackInterface) {
            getOperator().execute(new ConnectOperation(), aFeedBackInterface);
        }
    }

    private class EstadoConectado extends EstadoConexion {
    }
}
