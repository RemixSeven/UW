package cn.pan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
    private Socket client = null;

    public SocketClient(String site, int port) throws IOException {
        client = new Socket(site, port);
    }

    public void sendMsg(byte[] msg) throws IOException {
        OutputStream out = client.getOutputStream();
        out.write(msg);
        out.flush();
    }

    public void closeSocket() throws IOException {
        client.close();
    }

    public InputStream getInputStream() throws IOException {
        if (client != null) {
            return client.getInputStream();
        } else {
            throw new IllegalArgumentException("client is null.");
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (client != null) {
            return client.getOutputStream();
        } else {
            throw new IllegalArgumentException("client is null.");
        }
    }
}


