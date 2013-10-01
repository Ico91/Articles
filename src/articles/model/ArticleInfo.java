package articles.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "article_info")
public class ArticleInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(nullable = false)
	private int userId;
	@Column(nullable = false)
	private String articleId;

	public ArticleInfo() {

	}

	public ArticleInfo(int userId, String articleId) {
		this.userId = userId;
		this.articleId = articleId;
	}

	public int getUserId() {
		return this.userId;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + articleId.hashCode();
		result = prime * result + userId;
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleInfo other = (ArticleInfo) obj;
		if (other.articleId == this.articleId)
			return true;
		return false;
	}
}
