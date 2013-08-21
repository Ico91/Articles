package articles.model.dto.validators;

import java.util.List;

/**
 * Class used to build message from list of message keys
 * @author Krasimir Atanasov
 *
 */
public class MessageBuilder {
	private List<MessageKeys> messageKeys;
	
	public MessageBuilder(List<MessageKeys> messageKeys) {
		this.messageKeys = messageKeys;
	}
	
	public String getMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		for(MessageKeys key : this.messageKeys) {
			stringBuilder.append(key.getValue());
			stringBuilder.append(".");
			stringBuilder.append(System.lineSeparator());
		}
		
		return stringBuilder.toString();
	}
}