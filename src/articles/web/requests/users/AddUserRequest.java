package articles.web.requests.users;

import articles.dto.UserDetails;
import articles.model.User;

public class AddUserRequest extends UserRequest {

	public AddUserRequest(UserDetails dto) {
		super(dto);
	}

	@Override
	protected Object processEntity(User entity) {
		return this.dao.addUser(entity);
	}

}
