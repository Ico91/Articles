package articles.validators;

import java.util.List;

import articles.model.dto.ErrorMessage;

/**
 * Class used to build message from list of message keys
 * @author Krasimir Atanasov
 *
 */
public class ErrorMessageBuilder {
	private List<MessageKey> messageKeys;
	
	public ErrorMessageBuilder(List<MessageKey> messageKeys) {
		this.messageKeys = messageKeys;
	}
	
	public ErrorMessage getMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		for(MessageKey key : this.messageKeys) {
			stringBuilder.append(key.getValue());
			stringBuilder.append(".");
			stringBuilder.append(System.lineSeparator());
		}
		
		return new ErrorMessage(stringBuilder.toString());
	}
}