package com.bioodas.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

//FIXME µ•º¸ ‰»Î ‰≥ˆ
public class NioServer {
	public static void main(String[] args) throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.bind(new InetSocketAddress(666));
		System.err.println("Listening on port:" + channel.getLocalAddress());
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_ACCEPT);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		RequestHandler requestHandler = new RequestHandler();
		
		while(true) {
			int select = selector.select();
			if(select == 0) continue;
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = keys.iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				if(key.isAcceptable()) {
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
					SocketChannel clientChannel = serverSocketChannel.accept();
					clientChannel.configureBlocking(false);
					clientChannel.register(selector, SelectionKey.OP_READ);
				} 

				if(key.isReadable()) {
					SocketChannel socketChannel = (SocketChannel) key.channel();
					socketChannel.read(buffer);
					String request = new String(buffer.array()).trim();
					buffer.clear();
					System.err.println(String.format("Requst from %s: %s", socketChannel.getRemoteAddress(), request));
					String response = requestHandler.handler(request);
					socketChannel.write(ByteBuffer.wrap(response.getBytes()));
				}
				iterator.remove();
			}
		}

	}
}
