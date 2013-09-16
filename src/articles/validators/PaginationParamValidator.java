package articles.validators;

import java.util.ArrayList;
import java.util.List;

public class PaginationParamValidator implements Validator {
	private int from;
	private int to;
	private int totalResults;
	
	public PaginationParamValidator(int from, int to, int totalResults) {
		this.from = from;
		this.to = to;
		this.totalResults = totalResults;
	}
	
	@Override
	public List<MessageKey> validate() {
		List<MessageKey> messages= new ArrayList<MessageKey>();
		
		if(from < 0)
			messages.add(PaginationMessageKeys.NEGATIVE_FROM_PARAMETER);
		
		if(to < 0)
			messages.add(PaginationMessageKeys.NEGATIVE_TO_PARAMETER);
		
		if(from > to)
			messages.add(PaginationMessageKeys.FROM_GREATER_THAN_TO);
		
		if(from > totalResults)
			messages.add(PaginationMessageKeys.FROM_GREATER_THAN_TOTAL);
		
		if(from == to && from != 0)
			messages.add(PaginationMessageKeys.FROM_EQUALS_TO);
		
		return messages;
	}

}
