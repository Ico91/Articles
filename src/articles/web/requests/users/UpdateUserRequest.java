package articles.web.requests.users;

import java.util.List;

import articles.dto.MessageDTO;
import articles.dto.UserDetails;
import articles.model.User;
import articles.validators.Validator;

public class UpdateUserRequest extends UserRequest {
	private int userId;

	public UpdateUserRequest(UserDetails dto, int userId) {
		super(dto);
		this.userId = userId;
	}

	@Override
	protected Validator validator() {
		this.listOfUsers = removeCurrentUserFromList(this.userId,
				this.listOfUsers);
		return super.validator();
	}

	@Override
	protected Object processEntity(User entity) {
		return (this.dao.updateUser(this.userId, entity)) ? new MessageDTO(
				"User updated") : null;
	}

	private List<User> removeCurrentUserFromList(int id, List<User> users) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUserId() == id) {
				users.remove(i);
				break;
			}
		}

		return users;
	}
}
