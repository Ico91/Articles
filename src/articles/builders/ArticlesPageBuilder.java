package articles.builders;

import java.util.ArrayList;
import java.util.List;

import articles.dto.ResultDTO;
import articles.model.Articles.Article;

/**
 * Class used to build page with articles
 * @author Krasimir Atanasov
 *
 */
public class ArticlesPageBuilder {
	/**
	 * Build ResultDTO, containing all results between 'from' and 'to'
	 * and total possible results
	 * @param allResults All possible results
	 * @param from First result in page
	 * @param to Last result in page
	 * @return
	 */
	public ResultDTO<Article> buildResult(List<Article> allResults, int from, int to) {
		if(from > allResults.size())
			return new ResultDTO<Article>(new ArrayList<Article>(), allResults.size());
		//	List.sublist toIndex param is exclusive
		// to++;
		
		if(to <= 0 || to > allResults.size())
			to = allResults.size();

		return new ResultDTO<Article>(allResults.subList(from, to), allResults.size());
	}
}
