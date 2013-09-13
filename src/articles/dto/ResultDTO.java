package articles.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import articles.model.Articles.Article;

@XmlRootElement
@XmlSeeAlso(Article.class)
public class ResultDTO<T> {
	@XmlAnyElement
	private List<T> results;
	@XmlElement
	private long totalResults;

	public ResultDTO(List<T> results, long totalResults) {
		this.results = results;
		this.totalResults = totalResults;
	}

	public ResultDTO() {
		this.results = new ArrayList<T>();
		this.totalResults = 0;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
}
