package articles.web.requests.articles;

import java.util.List;

import articles.dao.ArticlesDAO;
import articles.model.Article;
import articles.validators.ArticleValidator;
import articles.validators.Validator;
import articles.web.requests.ResourceRequest;

public abstract class ArticleRequest extends ResourceRequest<Article, Article> {
	
	protected ArticlesDAO articlesDAO;
	protected List<Article> listOfArticles;
	protected List<Integer> userIds;
	
	public ArticleRequest(Article dto, String path) {
		super(dto);
		this.articlesDAO = new ArticlesDAO(path);
		this.listOfArticles = articlesDAO.getArticles();
	}

	@Override
	protected Article toEntity(Article dto) {
		dto.setId("");
		return dto;
	}

	@Override
	protected Validator validator() {
		return new ArticleValidator(toEntity(dto), this.listOfArticles);
	}

}
