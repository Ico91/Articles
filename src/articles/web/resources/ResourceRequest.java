package articles.web.resources;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.validators.MessageBuilder;
import articles.validators.MessageKey;
import articles.validators.Validator;

public abstract class ResourceRequest <T, E>{
	
	public Response process(T objectToValidate, List<E> listOfObject, Validator validator) {
		List<MessageKey> messageKeys = validator.validate();

		if (!messageKeys.isEmpty()) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity(new MessageBuilder(messageKeys)
							.getErrorMessage()).build();
		}
		
		return doProcess(objectToValidate, listOfObject);
	}
	
	public abstract Response doProcess(T objectToValidate, List<E> listOfObjects);
}
