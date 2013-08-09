package articles.dao;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import articles.model.Articles;
import articles.model.Articles.Article;

public class ArticlesDAO {
	private String articlesPath;

	public ArticlesDAO(String articlesPath) {
		this.articlesPath = articlesPath;
	}

	public List<Article> loadArticles(int id) {
		Articles articles = new Articles();
		try {

			File file = new File(pathToArticlesFile(id));
			if (file.exists()) {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Articles.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();
				articles = (Articles) jaxbUnmarshaller.unmarshal(file);
			}

		} catch (JAXBException e) {
			// TODO ???
			e.printStackTrace();
		}

		return articles.getArticle();
	}

	public void saveArticles(int id, List<Article> articlesList) {
		Articles article = new Articles();
		article.setArticle(articlesList);
		try {

			File file = new File(pathToArticlesFile(id));
			JAXBContext jaxbContext = JAXBContext.newInstance(Articles.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(article, file);

		} catch (JAXBException e) {
			// TODO: ???
			e.printStackTrace();
		}
	}

	private String pathToArticlesFile(int userId) {
		return this.articlesPath + userId + ".xml";
	}
}
