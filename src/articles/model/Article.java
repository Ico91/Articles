package articles.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Article {
	private String id;
	private String title;
	private String content;
	
	public Article() {
		
	}
	
	public Article(String id, String content, String title) {
		this.id = id;
		this.content = content;
		this.title = title;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
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
		Article other = (Article) obj;
		if(other.id == this.id)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "Article title: " + this.title + " Article content: " + this.content;
	}
}
