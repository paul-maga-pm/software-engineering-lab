package com.salesagents.networking.proxy;

import com.salesagents.business.utils.ProductObservable;
import com.salesagents.domain.models.Product;
import com.salesagents.exceptions.ExceptionBaseClass;
import com.salesagents.networking.protocols.RpcRequest;
import com.salesagents.networking.protocols.RpcResponse;
import com.salesagents.networking.protocols.RpcResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RpcClientStream extends ProductObservable {
    private int port;
    private String host;

    private volatile Socket clientSocket;
    private ObjectInputStream serverResponseStream;
    private ObjectOutputStream clientRequestStream;

    private BlockingQueue<RpcResponse> responseBlockingQueue;
    private volatile boolean isConnected;

    public RpcClientStream(String host, int port) {
        this.host = host;
        this.port = port;
        responseBlockingQueue = new LinkedBlockingQueue<>();
    }

    public void openConnection() {
        try {
            isConnected = true;
            clientSocket = new Socket(host, port);
            serverResponseStream = new ObjectInputStream(clientSocket.getInputStream());
            clientRequestStream = new ObjectOutputStream(clientSocket.getOutputStream());
            clientRequestStream.flush();
            startResponseReaderThread();
        } catch (IOException e) {
            throw new ExceptionBaseClass("Can't connect to server");
        }
    }

    private void startResponseReaderThread() {
        Thread responseReaderThread = new RpcResponseReader();
        responseReaderThread.start();
    }

    public void closeConnection() {
        try {
            clientSocket.close();
            serverResponseStream.close();
            clientRequestStream.flush();
            clientRequestStream.close();
            isConnected = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendRequest(RpcRequest request) {
        try {
            clientRequestStream.writeObject(request);
            clientRequestStream.reset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public RpcResponse readResponse() {
        try {
            return responseBlockingQueue.take();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private class RpcResponseReader extends Thread {
        @Override
        public void run() {
            while (isConnected) {
                try {
                    RpcResponse response = (RpcResponse) serverResponseStream.readObject();

                    if (response.isUpdateNotification())
                        handleUpdateNotification(response);
                    else {
                        try {
                            responseBlockingQueue.put(response);
                        } catch (InterruptedException e) {
                            System.out.println(e.toString());
                        }
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    System.out.println(exception.toString());
                }
            }
        }
    }

    private void handleUpdateNotification(RpcResponse response) {
        if (response.getType() == RpcResponseType.PRODUCT_WAS_ADDED)
            notifyThatProductWasAdded((Product) response.getData());
        else if (response.getType() == RpcResponseType.PRODUCT_WAS_REMOVED)
            notifyThatProductWasRemoved(response.getData().toString());
        else if (response.getType() == RpcResponseType.PRODUCT_WAS_UPDATED)
            notifyThatProductWasUpdated((Product) response.getData());
    }
}
