package ar.unlp.info.laboratorio.javaClickers.network.udp;

import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;

import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 01/06/13
 * Time: 21:40
 * To change this template use File | Settings | File Templates.
 */
public class Connection {

    public static int serverBroadcastPort = 45678;
    private static int clientBroadcastPort = 45680;

    /*public static InetSocketAddress discover(){
        DatagramPacket packet = (Receiver.receiveUDP(Sender.sendBroadcast(new Par<DatagramSocket, DatagramPacket>(Connection.getSocket(), new DatagramPacket(new byte[1024], 1024, null, Connection.serverBroadcastPort))))).getValue();
        return new InetSocketAddress(packet.getAddress(), new Integer(new String(packet.getData(), 0, packet.getLength())));
    }*/

    /*public static void serverBroadcastListening(Server server){
        Par<DatagramSocket, DatagramPacket> comInfo = Connection.receiveUDP(Connection.serverBroadcastPort);
        comInfo.getValue().setData(server.newClient().toString().getBytes());
        Sender.sendUDP(comInfo);
        comInfo.getKey().close();
    }*/

    public static Integer clientBroadcastListening(){
        return Integer.parseInt((Connection.receive(Connection.clientBroadcastPort)).getValue().getData().toString());
    }

    public static void sendBroadcast(Integer port){
        Sender.sendBroadcast(new Par<DatagramSocket, DatagramPacket>(Connection.getSocket(port), new DatagramPacket(new byte[1024], 1024)));
    }

    private static Par<DatagramSocket, DatagramPacket> receive(int port){
        return Receiver.receiveUDP(new Par<DatagramSocket, DatagramPacket>(Connection.getSocket(port), new DatagramPacket(new byte[1024], 1024)));
    }

    public static DatagramSocket getSocket(){
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return socket;
    }

    public static DatagramSocket getSocket(int port){
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return socket;
    }

}
