package articles.web.requests.users;

import articles.dao.ArticlesDAO;
import articles.dto.UserDetails;
import articles.model.User;
import articles.web.listener.ConfigurationListener;

public class AddUserRequest extends UserRequest {

	public AddUserRequest(UserDetails dto) {
		super(dto);
	}

	@Override
	protected Object processEntity(User entity) {
		ArticlesDAO articlesDAO = new ArticlesDAO(ConfigurationListener.getPath());
		User result = this.dao.addUser(entity);
		
		if(result != null)
			articlesDAO.createUserArticlesFile(result.getUserId());
		
		return result;
	}

}
