package com.bioodas.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static void main(String[] args) throws IOException {
		RequestHandler requestHandler = new RequestHandler();
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		try (ServerSocket socket = new ServerSocket(666)) { //FIXME----------<改行代码线程等待 开销比较大>
			System.err.println("Listening on port:" + socket.getLocalSocketAddress());
			while (true) {
				Socket client = socket.accept(); //FIXME ----------<改行代码线程等待 开销比较大>
				threadPool.submit(new ClientHandler(client, requestHandler));
			}
		}
	}
}
