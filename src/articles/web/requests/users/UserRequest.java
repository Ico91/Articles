package articles.web.requests.users;

import java.util.List;

import articles.dao.UserDAO;
import articles.dto.UserDetails;
import articles.model.User;
import articles.validators.UserValidator;
import articles.validators.Validator;
import articles.web.requests.ResourceRequest;

public abstract class UserRequest extends ResourceRequest<UserDetails, User> {
	protected UserDAO dao;
	protected List<User> listOfUsers;
	
	public UserRequest(UserDetails dto) {
		super(dto);
		this.dao = new UserDAO();
		this.listOfUsers = this.dao.getUsers();
	}

	@Override
	protected Validator validator() {
		return new UserValidator(dto, this.listOfUsers);
	}

	@Override
	protected User toEntity(UserDetails dto) {
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setUserType(dto.getType());
		
		return user;
	}
}
