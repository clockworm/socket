package com.bioodas.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{

	private final Socket client;
	private final RequestHandler requestHandler;
	
	public ClientHandler(Socket client, RequestHandler requestHandler) {
		this.client = client;
		this.requestHandler = requestHandler;
	}

	@Override
	public void run() {
		try (Scanner scaner = new Scanner(client.getInputStream())) {
			while (true) {
				String request = scaner.nextLine();
				if ("quit".equals(request))
					break;
				System.err.println(String.format("Requst from %s: %s", client.getRemoteSocketAddress(), request));
				String response = requestHandler.handler(request);
				client.getOutputStream().write(response.getBytes());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
