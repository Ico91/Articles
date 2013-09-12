package articles.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import articles.dao.exceptions.DAOException;
import articles.model.Articles.Article;

public class ArticlesDAOTest {

	private static String path = "/home/stinky/Desktop/Projects/Test";
	private static final int userId = 12;
	private static List<String> ids = new ArrayList<String>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		List<Article> listOfArticles = new ArrayList<Article>();
		for (int i = 0; i < 10; i++) {
			ids.add(UUID.randomUUID().toString());
			Article article = new Article();
			article.setId(ids.get(i));
			article.setContent("This is content for article " + i);
			article.setTitle("Title of article " + i);

			listOfArticles.add(article);
		}

		ArticlesDAO dao = new ArticlesDAO(path);
		dao.saveArticles(userId, listOfArticles);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ArticlesDAO dao = new ArticlesDAO(path);
		dao.deleteUserArticlesFile(userId);
	}

	@Test
	public void testLoadArticles() {
		ArticlesDAO dao = new ArticlesDAO(path);
		List<Article> listOfArticles = dao.loadUserArticles(userId);

		Assert.assertNotNull(listOfArticles);
		Assert.assertFalse(listOfArticles.isEmpty());
	}

	@Test
	public void testSaveArticles() {
		ArticlesDAO dao = new ArticlesDAO(path);
		List<Article> listOfArticles = dao.loadUserArticles(userId);
		int expected = listOfArticles.size();

		dao.saveArticles(userId + 1, listOfArticles);
		listOfArticles = dao.loadUserArticles(userId + 1);
		dao.deleteUserArticlesFile(userId + 1);

		Assert.assertTrue(expected == listOfArticles.size());
	}

	@Test
	public void testCreateUserArticlesFile() {
		ArticlesDAO dao = new ArticlesDAO(path);
		dao.createUserArticlesFile(500);
		File checkFile = new File(path + "/" + 500 + ".xml");

		if (!checkFile.exists()) {
			Assert.fail("File does not exist");
		}

		checkFile.delete();
	}

	@Test
	public void testGetArticleById() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article article = dao.getArticleById(ids.get(3));

		Assert.assertTrue(article.getId().equals(ids.get(3)));
		Assert.assertTrue(article.getTitle().equals("Title of article 3"));
	}

	@Test
	public void testAddArticle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article articleToAdd = new Article();
		articleToAdd.setContent("Some article");
		articleToAdd.setTitle("Some title");

		Article expected = dao.addArticle(userId, articleToAdd);
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

		dao.updateUserArticle(userId, expected);

		Article actual = dao.getArticleById(ids.get(1));

		Assert.assertTrue(actual.getTitle().equals(expected.getTitle())
				&& actual.getContent().equals(expected.getContent()));
	}

	@Test
	public void testDeleteArticle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		
		if(!dao.deleteUserArticle(userId, ids.get(5))) {
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
		
		dao.addArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void testAddNullTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("Test");
		
		dao.addArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void testAddEmptyTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("Test");
		invalid.setTitle("");
		
		dao.addArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void testAddEmptyContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setContent("");
		invalid.setTitle("Test");
		
		dao.addArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void testAddENullContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setTitle("Test");
		
		dao.addArticle(userId, invalid);
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
		Assert.assertFalse(dao.deleteUserArticle(userId, "kasjdfsfk-5842asfj-~!!!"));
	}
	
	@Test(expected = DAOException.class)
	public void updateWithInvalidId() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		invalid.setTitle("Testing title");
		invalid.setTitle("Some test title");
		dao.updateUserArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithNullContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setTitle("Some test title");
		invalid.setId(ids.get(3));
		dao.updateUserArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithEmptyContent() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setTitle("Some test title");
		invalid.setContent("");
		invalid.setId(ids.get(3));
		dao.updateUserArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithEmptyTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setTitle("");
		invalid.setContent("Test content");
		invalid.setId(ids.get(3));
		dao.updateUserArticle(userId, invalid);
	}
	
	@Test(expected = DAOException.class)
	public void updateWithNullTitle() {
		ArticlesDAO dao = new ArticlesDAO(path);
		Article invalid = new Article();
		
		invalid.setContent("Test content");
		invalid.setId(ids.get(3));
		dao.updateUserArticle(userId, invalid);
	}
}
