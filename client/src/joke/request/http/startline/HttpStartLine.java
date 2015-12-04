package joke.request.http.startline;

import joke.request.http.HttpRequestPotion;

public class HttpStartLine implements HttpRequestPotion {
	private HttpMethod method;
	private String target;
	private HttpVersion version;

	public HttpVersion getVersion() {
		return version;
	}

	public void setVersion(HttpVersion version) {
		this.version = version;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	@Override public String buildMessage() {
		StringBuilder message = new StringBuilder();
		message.append(method.name()).append(" ")
			.append(target).append(" ")
			.append(version.getVersion());
		return message.toString();
	}
}
