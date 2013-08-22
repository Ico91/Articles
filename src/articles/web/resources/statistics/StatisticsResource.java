package articles.web.resources.statistics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("")
public class StatisticsResource {

	@GET
	@Path("{userid}")
	@Produces("application/json")
	public Response getUserStatistics(@PathParam("userid") int userId,
			@QueryParam("date") DateAdapter dateInput) {
		//StatisticsStorage statistics = new StatisticsStorage();
		//Date date = dateInput.getDate();
		//List<UserStatisticsDTO> userStatistics = statistics.load(userId, date);
		//return Response.ok().entity(userStatistics.toString()).build();
		return null;
	}

}
