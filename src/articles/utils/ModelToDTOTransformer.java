package articles.utils;

import java.util.ArrayList;
import java.util.List;

import articles.dto.UserStatisticsDTO;
import articles.model.UserStatistics;

//	TODO: Comments
public class ModelToDTOTransformer {
	private ModelToDTOTransformer() {
		//	Empty
	}
	
	//	TODO: Comments
	public static List<UserStatisticsDTO> fillListOfStatisticsDTO(
			List<UserStatistics> listOfStatistics) {
		List<UserStatisticsDTO> listOfDTO = new ArrayList<UserStatisticsDTO>();

		for (UserStatistics us : listOfStatistics) {
			UserStatisticsDTO dto = new UserStatisticsDTO(us.getActivityDate(),
					us.getEvent(), us.getUserId());
			listOfDTO.add(dto);
		}

		return listOfDTO;
	}
}
