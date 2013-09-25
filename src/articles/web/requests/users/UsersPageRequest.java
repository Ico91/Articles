package articles.web.requests.users;

import articles.dao.UserDAO;
import articles.dto.ResultDTO;
import articles.model.User;
import articles.web.requests.PageRequest;

public class UsersPageRequest extends PageRequest {
	private String searchTerm;
	private UserDAO userDAO;
	
	public UsersPageRequest(int from, int to, UserDAO userDao, String searchTerm) {
		super(from, to);
		this.userDAO = userDao;
		this.searchTerm = searchTerm;
	}

	@Override
	protected Object doProcess() {
		return new ResultDTO<User>(userDAO.getUsers(searchTerm, from, to),
				userDAO.getUsers(searchTerm, 0, 0).size());
	}

}
