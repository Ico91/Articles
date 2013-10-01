package articles.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import articles.model.Article;
import articles.model.User;

@XmlRootElement
@XmlSeeAlso({Article.class, User.class, UserStatisticsDTO.class})
public class ResultDTO<T> {
	private List<T> results;
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
	
	@XmlAnyElement
	public List<T> getResults() {
		return this.results;
	}
	
	public long getTotalResults() {
		return this.totalResults;
	}
}
