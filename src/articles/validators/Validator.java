package articles.validators;

import java.util.List;

import articles.messages.MessageKey;

public interface Validator {
	List<MessageKey> validate();
}
