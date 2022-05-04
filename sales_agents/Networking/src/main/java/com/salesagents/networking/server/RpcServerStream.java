package com.salesagents.networking.server;

import com.salesagents.networking.protocols.RpcRequest;
import com.salesagents.networking.protocols.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcServerStream {
    private Socket clientSocket;
    private ObjectInputStream clientRequestStream;
    private ObjectOutputStream serverResponseStream;

    public RpcServerStream(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void openObjectStreams() throws IOException {
        serverResponseStream = new ObjectOutputStream(clientSocket.getOutputStream());
        serverResponseStream.flush();
        clientRequestStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void closeObjectStreams() throws IOException {
        clientRequestStream.close();
        serverResponseStream.flush();
        serverResponseStream.close();
        clientSocket.close();
    }

    public void sendResponse(RpcResponse response) throws IOException {
        serverResponseStream.writeObject(response);
        serverResponseStream.flush();
    }

    public RpcRequest readRequest() throws IOException, ClassNotFoundException {
        return (RpcRequest) clientRequestStream.readObject();
    }
}
