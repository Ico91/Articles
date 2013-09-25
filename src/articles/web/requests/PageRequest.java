package articles.web.requests;

import articles.validators.PageParametersValidator;
import articles.validators.Validator;

public abstract class PageRequest extends BusinessRequest {
	protected int from;
	protected int to;
	
	public PageRequest(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	protected Validator validator() {
		return new PageParametersValidator(this.from, this.to);
	}
}
