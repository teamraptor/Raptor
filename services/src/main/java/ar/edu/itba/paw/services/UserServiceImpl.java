package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDao;

    //test
    void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }
    
	@Transactional
    @Override
    public User register(final String username, final String password, final String email, final String firstName, final String lastName) {
        User user = userDao.create(username, password, email, firstName, lastName);
        if (user == null) {
            //TODO handle null
        }

        return user;
    }

	@Transactional
    @Override
    public List<User> searchUsers(final String text, final int resultsPerPage, final int page) {
        List<User> ans = userDao.searchUsers(text, resultsPerPage, page);
        if (ans == null) {
            //TODO handle null
        }
        return ans;
    }

	@Transactional
    @Override
    public User authenticateUser(final String username, final String password) {
        return userDao.authenticateUser(username, password);
    }

    @Override
    public User getUserWithId(final String userId) {
        return null; //TODO is needed?
    }

	@Transactional
    @Override
    public User getUserWithUsername(final String username) {
        return userDao.getByUsername(username);
    }
	
	@Transactional
    @Override
    public List<User> getFollowers(final String userId, final int resultsPerPage, final int page) {
        List<User> ans = userDao.getFollowers(userId, resultsPerPage, page);
        if (ans == null) {
            //TODO handle null
        }
        return ans;
    }

	@Transactional
    @Override
    public List<User> getFollowing(final String userId, final int resultsPerPage, final int page) {
        List<User> ans = userDao.getFollowing(userId, resultsPerPage, page);
        if (ans == null) {
            //TODO handle null
        }
        return ans;
    }

}
