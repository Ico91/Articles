package articles.web.resources;

import java.util.Date;

import javax.ws.rs.core.Response;

import articles.model.UserActivity;

public abstract class StatisticsRequest {
	
	public abstract Response execute(Date dateInput, UserActivity activity);

	public Response getStatistics(DateAdapter dateInput, UserActivity activity) {
		if (dateInput == null)
			return execute(null, activity);
		else if (activity == null) 
			return execute(dateInput.getDate(), null);
		
		return execute(dateInput.getDate(), activity);
	}
}
