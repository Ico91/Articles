package articles.web.resources;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.validators.MessageBuilder;
import articles.validators.MessageKey;
import articles.validators.PageParametersValidator;

public abstract class PageRequest<T> {
	
	public Response process(int from, int to) {
		List<MessageKey> messageKeys = new PageParametersValidator(from, to).validate();

		if (!messageKeys.isEmpty()) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity(new MessageBuilder(messageKeys)
							.getErrorMessage()).build();
		}
		
		return doProcess(from, to);
	}
	
	public abstract Response doProcess(int from, int to);
}
