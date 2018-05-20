package oio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//单线程情况下只能有一个客户端用线程池可以有多个客户端连接，但是非常消耗性能
public class OioServer {

	public OioServer() {

	}

	public static void main(String[] args) throws IOException {
		
		ExecutorService newPool=Executors.newCachedThreadPool();
		
		ServerSocket server = new ServerSocket(10101);
		while (true) {
			final Socket socket = server.accept();
			newPool.execute(new Runnable() {
				
				@Override
				public void run() {
					handel(socket);
				}
			});
		}
		
	}

	private static void handel(Socket socket) {
		try {
			byte[] bytes = new byte[1024];
			InputStream inputStream = socket.getInputStream();
			while (true) {
				int read = inputStream.read(bytes);
				if (read != 1) {
					System.out.println(new String(bytes, 0, read));
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
