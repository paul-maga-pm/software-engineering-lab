package com.salesagents.networking.server;

import com.salesagents.business.administrator.services.AdministratorLoginService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer {
    private static final int WORKER_THREAD_POOL_SIZE = 10;

    private int port;
    private ExecutorService workersThreadPool;
    private ServerSocket serverSocket;

    private AdministratorLoginService adminLoginService;


    public RpcServer(int port) {
        this.port = port;
        workersThreadPool = Executors.newFixedThreadPool(WORKER_THREAD_POOL_SIZE);
    }

    public void setAdminLoginService(AdministratorLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }

        System.out.println("Listening on port " + port);
        System.out.println("Waiting for clients...");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                RpcServerStream serverStream = new RpcServerStream(clientSocket);
                serverStream.openObjectStreams();

                RpcWorker worker = new RpcWorker(serverStream);

                worker.setAdminLoginService(adminLoginService);
                workersThreadPool.execute(worker);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
