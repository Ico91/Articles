package articles.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import articles.messages.MessageKey;

@XmlRootElement
public class MessageDTO {
	@XmlElement
	private List<String> messages;

	public MessageDTO() {
		this.messages = new ArrayList<String>();
	}

	public MessageDTO(List<MessageKey> messageKeys) {
		this.messages = new ArrayList<String>();
		buildList(messageKeys);
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	private void buildList(List<MessageKey> messageKeys) {
		for(MessageKey m : messageKeys)
			this.messages.add(m.getValue());
	}
}
