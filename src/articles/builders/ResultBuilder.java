package articles.builders;

import java.util.ArrayList;
import java.util.List;

import articles.dto.ResultDTO;

public class ResultBuilder<T> {
	public ResultDTO<T> buildResult(List<T> allResults, int from, int to) {
		if(from > allResults.size())
			return new ResultDTO<T>(new ArrayList<T>(), allResults.size());
		
		if(to <= 0)
			to = allResults.size();

		if(to > allResults.size())
			to = allResults.size();

		return new ResultDTO<T>(allResults.subList(from, to), allResults.size());
	}
}
