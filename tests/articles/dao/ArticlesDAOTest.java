package articles.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.dao.exceptions.DAOException;
import articles.model.Article;

public class ArticlesDAOTest {

	private static String path = "TestsFolder/";
	private static final int userId = 1;
	private static List<String> ids = new ArrayList<String>();
	private static List<Integer> userIds = new ArrayList<Integer>();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File dir = new File(path, "");
		dir.mkdir();
		
		ArticlesDAO dao = new ArticlesDAO(path);
		
		for (int i = 0; i < 10; i++) {
			userIds.add(i);
			Article article = new Article();
			article.setId(ids.get(i));
			article.setContent("This is content for article " + i);
			article.setTitle("Title of article " + i);

			ids.add(dao.addArticle(article, userId).getId());
		}
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ArticlesDAO dao = new ArticlesDAO(path);
		for(String s : ids)
			dao.deleteArticle(s, 1);
	}

	@Test
	public void testLoadArticles() {
		ArticlesDAO dao = new ArticlesDAO(path);
		List<Article> listOfArticles = dao.getArticles();

		Assert.assertNotNull(listOfArticles);
		Assert.assertFalse(listOfArticles.isEmpty());
	}

	@Test
	public void testGetArticleById() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article article = dao.getArticleById(ids.get(0));

		Assert.assertTrue(article.getId().equals(ids.get(0)));
		Assert.assertTrue(article.getTitle().equals("Title of article 0"));
	}

	@Test
	public void testAddArticle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article articleToAdd = new Article();
		articleToAdd.setContent("Some article");
		articleToAdd.setTitle("Some title");

		Article expected = dao.addArticle(articleToAdd, userId);
		Article actual = dao.getArticleById(expected.getId());

		Assert.assertTrue(actual.getTitle().equals(expected.getTitle())
				&& actual.getContent().equals(expected.getContent()));
	}

	@Test
	public void testUpdateArticle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article expected = new Article();
		expected.setId(ids.get(1));
		expected.setContent("Updated content");
		expected.setTitle("Updated title");

		dao.updateArticle(userId, expected);

		Article actual = dao.getArticleById(ids.get(1));

		Assert.assertTrue(actual.getTitle().equals(expected.getTitle())
				&& actual.getContent().equals(expected.getContent()));
	}

	@Test
	public void testDeleteArticle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		
		if(!dao.deleteArticle(ids.get(5), userId)) {
			Assert.fail("Failed to delete article");
		}
		
		Article result = dao.getArticleById(ids.get(5));
		Assert.assertTrue(result == null);
	}

	@Test(expected = DAOException.class)
	public void testAddDuplicatedArticleTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("Test");
		invalid.setTitle("Title of article 3");
		
		dao.addArticle(invalid, userId);
	}
	
	@Test(expected = DAOException.class)
	public void testAddNullTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("Test");
		
		dao.addArticle(invalid, userId);
	}
	
	@Test(expected = DAOException.class)
	public void testAddEmptyTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("Test");
		invalid.setTitle("");
		
		dao.addArticle(invalid, userId);
	}
	
	@Test(expected = DAOException.class)
	public void testAddEmptyContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("");
		invalid.setTitle("Test");
		
		dao.addArticle(invalid, userId);
	}
	
	@Test(expected = DAOException.class)
	public void testAddENullContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setTitle("Test");
		
		dao.addArticle(invalid, userId);
	}
	
	@Test
	public void getInvalidArticleId() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article expected = dao.getArticleById("4!42-1235-1231235-325sc");
		Assert.assertTrue(expected == null);
	}
	
	@Test
	public void deleteInvalidArticleId() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Assert.assertFalse(dao.deleteArticle("kasjdfsfk-5842asfj-~!!!", userId));
	}
	
	@Test(expected = DAOException.class)
	public void updateWithInvalidId() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setTitle("Testing title");
		invalid.setTitle("Some test title");
		dao.updateArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithNullContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setTitle("Some test title");
		invalid.setId(ids.get(3));
		dao.updateArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithEmptyContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setTitle("Some test title");
		invalid.setContent("");
		invalid.setId(ids.get(3));
		dao.updateArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithEmptyTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setTitle("");
		invalid.setContent("Test content");
		invalid.setId(ids.get(3));
		dao.updateArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithNullTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setContent("Test content");
		invalid.setId(ids.get(3));
		dao.updateArticle(userId, invalid);
	}
}
