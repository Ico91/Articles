package articles.web.requests;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import articles.validators.MessageBuilder;
import articles.validators.MessageKey;
import articles.validators.Validator;

public abstract class BusinessRequest {
	public Response process() {
		List<MessageKey> listOfKeys = validator().validate();
		
		if(!listOfKeys.isEmpty()) {
			return Response.status(Status.BAD_REQUEST)
					.entity(new MessageBuilder(listOfKeys)
					.getErrorMessage()).build();
		}
		Object result = doProcess();
		return (result != null) ? Response.ok(result, MediaType.APPLICATION_JSON).build()
				: Response.status(Status.BAD_REQUEST).build();
	}
	
	protected abstract Validator validator();
	protected abstract Object doProcess();
}
