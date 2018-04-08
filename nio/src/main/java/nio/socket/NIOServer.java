package nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

	private int flag = 1;
	private int blockSize = 4096;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
	private ByteBuffer receivebuffer = ByteBuffer.allocate(blockSize);
	private Selector selector;

	public NIOServer(int port) throws IOException {

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 是否阻塞
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(port));
		// 打开选择器
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("Server start->" + port);
	}

	public void listen() throws IOException {
		while (true) {
			selector.select();
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				iterator.remove();
				handleKey(selectionKey);
			}
		}
	}

	private void handleKey(SelectionKey selectionKey) throws IOException {
		ServerSocketChannel server = null;
		SocketChannel client = null;
		String reciveText;
		String sendText;
		int count = 0;
		if (selectionKey.isAcceptable()) {
			server = (ServerSocketChannel) selectionKey.channel();
			client = server.accept();
			client.configureBlocking(false);
			client.register(selector, selectionKey.OP_READ);
		} else if (selectionKey.isReadable()) {
			client = (SocketChannel) selectionKey.channel();
			count = client.read(receivebuffer);
			if (count > 0) {
				reciveText = new String(receivebuffer.array(), 0, count);
				System.out.println("服务端接收客户端信息:" + reciveText);
				client.register(selector, selectionKey.OP_WRITE);
			}
		} else if (selectionKey.isWritable()) {
			sendBuffer.clear();
			client=(SocketChannel) selectionKey.channel();
			sendText="msg send to client:"+flag++;
			sendBuffer.put(sendText.getBytes());
			sendBuffer.flip();
			client.write(sendBuffer);
			System.out.println("服务端接收客户端信息:" + sendText);
		}
	}

	public static void main(String[] args) throws IOException {
		int port=7080;
		NIOServer server=new NIOServer(port);
		server.listen();
	}
}
