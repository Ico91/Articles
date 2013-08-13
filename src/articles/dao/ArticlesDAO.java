package articles.dao;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import articles.dao.exceptions.ArticlesDAOException;
import articles.model.Articles;
import articles.model.Articles.Article;

public class ArticlesDAO {
	private String articlesPath;

	public ArticlesDAO(String articlesPath) {
		this.articlesPath = articlesPath;
	}

	public List<Article> loadArticles(int userId) throws ArticlesDAOException {
		Articles articles = new Articles();
		try {

			File file = new File(pathToArticlesFile(userId));
			if (file.exists()) {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Articles.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();
				articles = (Articles) jaxbUnmarshaller.unmarshal(file);
			}

		} catch (JAXBException e) {
			throw new ArticlesDAOException("Invalid articles.xml file format");
		}

		return articles.getArticle();
	}

	public int addArticle(int userId, Article article)
			throws ArticlesDAOException {
		if (validArticle(article)) {
			if (emptyTitle(article))
				throw new ArticlesDAOException("Article title cannot be empty.");
			List<Article> articles = loadArticles(userId);
			
			if (hasUniqueTitle(article, articles) == false)
				throw new ArticlesDAOException(
						"Article with same title already exist!");

			article.setId(generateArticleId(articles));
			articles.add(article);
			saveArticles(userId, articles);

			return article.getId();
		} else
			throw new ArticlesDAOException("Invalid article format");
	}
	
	public void saveArticles(int userId, List<Article> articlesList)
			throws ArticlesDAOException {
		Articles article = new Articles();
		article.setArticle(articlesList);
		try {

			File file = new File(pathToArticlesFile(userId));
			JAXBContext jaxbContext = JAXBContext.newInstance(Articles.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(article, file);

		} catch (JAXBException e) {
			throw new ArticlesDAOException("Invalid articles format.");
		}
	}

	private boolean hasUniqueTitle(Article article, List<Article> articles) {
		for (Article a : articles) {
			if (a.getTitle().equals(article.getTitle())) {
				System.out.println(articles.size());
				return false;
			}
		}
		return true;
	}

	private int generateArticleId(List<Article> articles) {
		int max = Integer.MIN_VALUE;

		for (Article a : articles) {
			if (a.getId() > max)
				max = a.getId();
		}
		if (max < 1)
			return 1;

		return ++max;
	}

	private boolean emptyTitle(Article article) {
		return (article.getTitle().equals(""));
	}

	private boolean validArticle(Article article) {
		return (article.getContent() != null && article.getTitle() != null);
	}

	private String pathToArticlesFile(int userId) {
		return this.articlesPath + "/" + userId + ".xml";
	}
}
