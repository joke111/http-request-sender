package joke.lib.message.response.http;

import joke.lib.message.general.http.header.HttpHeader;
import joke.lib.message.general.http.header.HttpHeaders;
import joke.lib.message.general.http.payload.HttpPayload;
import joke.lib.message.general.http.startline.HttpVersion;
import joke.lib.message.response.Response;
import joke.lib.message.response.http.startline.HttpResponseStartLine;
import joke.lib.message.response.http.startline.HttpStatus;

import java.util.*;

public class HttpResponse implements Response {
	private HttpResponseStartLine startLine;
	private HttpHeaders headers;
	private HttpPayload payload;

	private HttpResponse(HttpResponseStartLine startLine, HttpHeaders headers, HttpPayload payload) {
		Objects.requireNonNull(startLine, "Start line should not be null");

		this.startLine = startLine;
		this.headers = headers;
		this.payload = payload;
	}

	public HttpResponseStartLine getStartLine() {
		return startLine;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public HttpPayload getPayload() {
		return payload;
	}

	public HttpStatus getStatus() {
		return startLine.getStatus();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private HttpVersion version;
		private HttpStatus status;
		private Map<String, HttpHeader> headers;
		private String payload;

		public Builder() {
			this.headers = new LinkedHashMap<>();
		}

		public HttpResponse build() {
			HttpResponseStartLine startLine = new HttpResponseStartLine();
			startLine.setStatus(status);
			startLine.setVersion(version);

			HttpHeaders headers = new HttpHeaders(this.headers);

			HttpPayload payload = new HttpPayload(this.payload);

			return new HttpResponse(startLine, headers, payload);
		}

		public Builder version(String version) {
			this.version = HttpVersion.convertToVersion(version);
			return this;
		}

		public Builder statusCode(String status) {
			this.status = HttpStatus.convertToStatus(status);
			return this;
		}

		public Builder addHeader(HttpHeader header) {
			this.headers.put(header.getName(), header);
			return this;
		}

		public Builder payload(String payload) {
			this.payload = payload;
			return this;
		}
	}
}
