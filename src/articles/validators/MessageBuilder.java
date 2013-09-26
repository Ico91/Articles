package articles.validators;

import java.util.List;

import articles.dto.MessageDTO;

/**
 * Class used to build message from list of message keys
 * 
 * @author Krasimir Atanasov
 * 
 */
public class MessageBuilder {
	private List<MessageKey> messageKeys;

	public MessageBuilder(List<MessageKey> messageKeys) {
		this.messageKeys = messageKeys;
	}

	public MessageDTO getErrorMessage() {
		return new MessageDTO(messageKeys);
	}
}