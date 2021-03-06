package joke.lib.server.nonblocking.classic.tcp;

import joke.lib.message.request.Request;
import joke.lib.message.request.parser.RequestParser;
import joke.lib.message.response.DefaultResponse;
import joke.lib.message.response.Response;
import joke.lib.server.ServerWorker;
import joke.lib.server.blocking.tcp.BlockingTcpServer;
import joke.lib.server.nonblocking.classic.SocketChannelWrapper;
import joke.lib.util.CloseableUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NonBlockingClassicTcpServerWorker<Q extends Request, P extends Response> implements ServerWorker<Q, P> {

	private final RequestParser<Q> parser;

	public NonBlockingClassicTcpServerWorker(RequestParser<Q> parser) {
		this.parser = parser;
	}

	public void work(SocketChannelWrapper socketChannel) {
		try {
			String input = read(socketChannel.getSocketChannel());
			if (input.isEmpty()) {
				if(shouldSocketBeClosedWhenRequestIsEnded()) {
					socketChannel.close();
				}
				socketChannel.setInProgress(false);
				return;
			}

			Q request = parser.parse(input);

			P response = handleRequest(request);
			socketChannel.write(ByteBuffer.wrap(response.getMessage().getBytes()));

			if (shouldSocketBeClosedWhenRequestIsEnded()) {
				socketChannel.close();
			}
			socketChannel.setInProgress(false);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (shouldSocketBeClosedWhenRequestIsEnded()) {
				CloseableUtils.closeQuietly(socketChannel);
			}
			socketChannel.setInProgress(false);
		}
	}

	private String read(SocketChannel socketChannel) throws IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int count;
			while ((count = socketChannel.read(buffer)) > 0) {
				buffer.flip();
				outputStream.write(buffer.array(), 0, count);
				buffer.clear();
			}
			return new String(outputStream.toByteArray(), BlockingTcpServer.DEFAULT_CHARSET);
		}
	}

	public P handleRequest(Q request) {
		return (P) new DefaultResponse("???");
	}

	protected boolean shouldSocketBeClosedWhenRequestIsEnded() {
		return false;
	}
}
