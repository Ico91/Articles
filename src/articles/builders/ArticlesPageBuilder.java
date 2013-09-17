package articles.builders;

import java.util.ArrayList;
import java.util.List;

import articles.dto.ResultDTO;
import articles.model.Articles.Article;

public class ArticlesPageBuilder {
	public ResultDTO<Article> buildResult(List<Article> allResults, int from, int to) {
		if(from > allResults.size())
			return new ResultDTO<Article>(new ArrayList<Article>(), allResults.size());
		
		if(to <= 0 || to > allResults.size())
			to = allResults.size();

		return new ResultDTO<Article>(allResults.subList(from, to), allResults.size());
	}
}
