package articles.web.requests.articles;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.dao.ArticlesDAO;
import articles.dto.ResultDTO;
import articles.model.Article;

public class ArticlesPageRequestTests {

	private static ArticlesDAO articlesDao;
	private static String path = "TestsFolder";
	private static int userId = 1;
	private static List<String> ids;

	@BeforeClass
	public static void setUp() {
		ids = new ArrayList<String>();
		File dir = new File(path, "");
		dir.mkdir();

		articlesDao = new ArticlesDAO(path + "/");

		for (int i = 0; i < 20; i++) {
			Article article = new Article();
			article.setContent("Content for article " + (i + 1));
			article.setTitle("Title for article " + (i + 1));
			articlesDao.addArticle(article, userId);
		}
	}

	@AfterClass
	public static void clear() {
		for (String s : ids) {
			articlesDao.deleteArticle(s, 1);
		}
		File dir = new File(path);
		dir.delete();
	}

	@Test
	public void testArticlesPage() {
		int expectedSize = 6;
		String expectedFirstTitle = "Title for article 6";
		String expectedLastTitle = "Title for article 11";
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(1);

		@SuppressWarnings("unchecked")
		ResultDTO<Article> result = (ResultDTO<Article>) new ArticlesPageRequest(
				5, 10, userId, path + "/").process().getEntity();

		assertTrue(expectedSize == result.getResults().size());
		assertTrue(result.getResults().get(0).getTitle()
				.equals(expectedFirstTitle));
		assertTrue(result.getResults().get(5).getTitle()
				.equals(expectedLastTitle));
	}
}
