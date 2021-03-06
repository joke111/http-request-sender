package joke.lib.server.nonblocking.classic.http;

import joke.lib.message.request.http.HttpRequest;
import joke.lib.message.request.parser.RequestParser;
import joke.lib.message.response.http.HttpResponse;
import joke.lib.message.response.http.startline.HttpStatus;
import joke.lib.server.nonblocking.classic.tcp.NonBlockingClassicTcpServerWorker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

public class NonBlockingClassicHttpServerWorker extends NonBlockingClassicTcpServerWorker<HttpRequest, HttpResponse> {
	public NonBlockingClassicHttpServerWorker(RequestParser<HttpRequest> parser) {
		super(parser);
	}

	@Override public HttpResponse handleRequest(HttpRequest request) {
		try {
			String target = request.getStartLine().getTarget();
			if (target.startsWith("/")) {
				target = target.replaceFirst("/", "");
			}
			URL targetResource = getClass().getClassLoader().getResource(target);
			if (targetResource == null) {
				throw new FileNotFoundException("Target file is not exist");
			}
			File targetFile = new File(targetResource.getFile());

			BufferedReader reader = new BufferedReader(new FileReader(targetFile));
			String data;
			StringBuilder fileData = new StringBuilder();
			while ((data = reader.readLine()) != null) {
				fileData.append(data);
			}

			return HttpResponse.builder()
				.statusCode(HttpStatus.OK)
				.version(request.getStartLine().getVersion())
				.payload(fileData.toString())
				.build();
		} catch (FileNotFoundException exception) {
			return HttpResponse.builder()
				.statusCode(HttpStatus.NOT_FOUND)
				.version(request.getStartLine().getVersion())
				.build();
		} catch (Exception exception) {
			return HttpResponse.builder()
				.statusCode(HttpStatus.INTERNAL)
				.version(request.getStartLine().getVersion())
				.payload(exception.getMessage())
				.build();
		}
	}

	@Override
	protected boolean shouldSocketBeClosedWhenRequestIsEnded() {
		return true;
	}
}
