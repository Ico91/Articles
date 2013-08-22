package articles.web.resources.statistics;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import articles.dao.StatisticsDAO;
import articles.statistics.dto.UserStatisticsDTO;

@Path("")
public class StatisticsResource {

	@GET
	@Path("{userid}")
	@Produces("application/json")
	public Response getUserStatistics(@PathParam("userid") int userId,
			@QueryParam("date") DateAdapter dateInput) {

		StatisticsDAO statistics = new StatisticsDAO();
		if(dateInput != null) {
			Date date = dateInput.getDate();
			List<UserStatisticsDTO> userStatistics = statistics.load(userId, date);
			return Response.ok().entity(userStatistics.toString()).build();
		}
		
		return Response.status(400).entity("No date provided").build();
	}

}
