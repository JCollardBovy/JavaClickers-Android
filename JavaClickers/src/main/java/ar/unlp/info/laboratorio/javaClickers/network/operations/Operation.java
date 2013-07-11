package ar.unlp.info.laboratorio.javaClickers.network.operations;

import java.io.Serializable;

/**
 * Created by Jony on 02/06/13.
 */
public interface Operation extends Serializable{

    public static final long serialVersionUID = 87L;

    public void executeOnClient();
    public void executeOnServer(Serviceable serverInterface);

}
