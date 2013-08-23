package articles.web.resources.statistics;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import articles.dao.StatisticsDAO;
import articles.statistics.dto.UserStatisticsDTO;
import articles.web.listener.ConfigurationListener;

/**
 * Provides methods for accessing statistics of user activities individually or
 * global statistics of all users.
 * 
 * @author Hristo
 * 
 */
@Path("")
public class StatisticsResource {
	@Context
	private HttpSession session;

	/**
	 * Returns statistics information for the currently logged user.
	 * 
	 * @param dateInput
	 *            - date to load the statistics for. The specified date is
	 *            required in the ISO format (yyyy/mm/dd)
	 * @return If successful returns JSON representation of a list of user
	 *         activities, otherwise returns status code 400 (Bad request).
	 */
	@GET
	@Produces("application/json")
	public Response getUserStatistics(@QueryParam("date") DateAdapter dateInput) {

		if (dateInput == null)
			return Response.status(400).entity("No date provided").build();

		Date date = dateInput.getDate();
		StatisticsDAO statistics = new StatisticsDAO();
		List<UserStatisticsDTO> userStatistics = statistics.load(
				(int) session.getAttribute(ConfigurationListener.USERID), date);
		return Response.ok().entity(userStatistics).build();
	}

}
