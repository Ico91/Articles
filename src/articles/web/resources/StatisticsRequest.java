package articles.web.resources;

import java.util.Date;

import javax.ws.rs.core.Response;

public abstract class StatisticsRequest {
	
	public abstract Response execute(Date dateInput);

	public Response getStatistics(DateAdapter dateInput) {
		if (dateInput == null)
			return Response.status(400).entity("No date provided").build();
		
		return execute(dateInput.getDate());
	}
}
