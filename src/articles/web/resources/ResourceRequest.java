package articles.web.resources;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.validators.MessageBuilder;
import articles.validators.MessageKey;
import articles.validators.Validator;

public abstract class ResourceRequest <T, E>{
	
	public Response process(T object, List<E> listOfObject, Validator validator) {
		List<MessageKey> messageKeys = validator.validate();

		if (!messageKeys.isEmpty()) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity(new MessageBuilder(messageKeys)
							.getErrorMessage()).build();
		}
		
		return doProcess(object, listOfObject);
	}
	
	public abstract Response doProcess(T object, List<E> listOfObjects);
}
