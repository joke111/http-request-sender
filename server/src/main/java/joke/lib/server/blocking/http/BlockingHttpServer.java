package joke.lib.server.blocking.http;

import joke.lib.message.request.http.HttpRequest;
import joke.lib.message.request.parser.HttpRequestParser;
import joke.lib.message.response.http.HttpResponse;
import joke.lib.server.blocking.tcp.BlockingTcpServer;

import java.net.Socket;

public class BlockingHttpServer extends BlockingTcpServer<HttpRequest, HttpResponse> {

	public static final int DEFAULT_PORT = 80;

	public BlockingHttpServer() {
		this(DEFAULT_PORT);
	}

	public BlockingHttpServer(int port) {
		super(port, new HttpRequestParser());
	}

	@Override protected Thread createWorkerThread(Socket socket) {
		return new Thread(() -> new BlockingHttpServerWorker(parser).work(socket));
	}
}
