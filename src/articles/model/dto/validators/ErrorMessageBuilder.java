package articles.model.dto.validators;

import java.util.List;

import articles.model.dto.ErrorMessage;

/**
 * Class used to build message from list of message keys
 * @author Krasimir Atanasov
 *
 */
public class ErrorMessageBuilder {
	private List<MessageKeys> messageKeys;
	
	public ErrorMessageBuilder(List<MessageKeys> messageKeys) {
		this.messageKeys = messageKeys;
	}
	
	public ErrorMessage getMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		for(MessageKeys key : this.messageKeys) {
			stringBuilder.append(key.getValue());
			stringBuilder.append(".");
			stringBuilder.append(System.lineSeparator());
		}
		
		return new ErrorMessage(stringBuilder.toString());
	}
}