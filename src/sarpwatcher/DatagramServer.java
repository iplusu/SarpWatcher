/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
/**
 *
 * @author Administrator
 */
public class DatagramServer extends Thread {
    private DatagramSocket socket;
    private SarpWatcher parent;
    private boolean isRunning;

    public DatagramServer(SarpWatcher parent) throws SocketException {
        super();
        // DatagramPacket을 받기 위한 Socket 생성
        // 9999 : Listen할 Port
        socket = new DatagramSocket(12000);
        this.parent = parent;
        isRunning = true;
    }

    public void StopServer() {
        socket.close();
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning)
        {
            try
            {
                // 데이터를 받을 버퍼
                byte[] inbuf = new byte[512];
                // 데이터를 받을 Packet 생성
                DatagramPacket packet = new DatagramPacket(inbuf, inbuf.length);
                // 데이터 수신
                // 데이터가 수신될 때까지 대기됨
                socket.receive(packet);
                parent.msgHandler(new String(packet.getData(), 0, packet.getLength()));
            } catch (IOException e)
            {
//                                e.printStackTrace();
            }
        }
    }
}