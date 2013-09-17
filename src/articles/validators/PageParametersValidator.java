package articles.validators;

import java.util.ArrayList;
import java.util.List;

public class PageParametersValidator implements Validator {
	private int from;
	private int to;
	
	public PageParametersValidator(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public List<MessageKey> validate() {
		List<MessageKey> messages= new ArrayList<MessageKey>();
		
		if(from < 0)
			messages.add(PageMessageKeys.NEGATIVE_FROM_PARAMETER);
		
		if(to < 0)
			messages.add(PageMessageKeys.NEGATIVE_TO_PARAMETER);
		
		if(from == to && from != 0)
			messages.add(PageMessageKeys.FROM_EQUALS_TO);
		
		if(to < from && to != 0)
			messages.add(PageMessageKeys.TO_LOWER_THAN_FROM);
		
		return messages;
	}

}
