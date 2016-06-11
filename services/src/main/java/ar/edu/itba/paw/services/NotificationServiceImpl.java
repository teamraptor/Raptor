package ar.edu.itba.paw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.models.NotificationType;
import ar.edu.itba.paw.models.Tweet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.NotificationDAO;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDAO notifDAO;
	
	@Transactional
	@Override
	public void register(final User from, final User to, final NotificationType type, final Tweet tweet) {
		Notification notif = notifDAO.create(from, to, type, tweet);
		if(notif == null){
			//TODO handle null
		}
	}

	@Transactional
	@Override
	public void seen(final Notification notif) {
		notifDAO.seen(notif);
	}

	@Transactional
	@Override
	public void notSeen(final Notification notif) {
		notifDAO.notSeen(notif);
	}

	@Transactional
	@Override
	public List<Notification> getNotifications(final User user) {
		List<Notification> ans = notifDAO.getNotifications(user);
        if (ans == null) {
            // TODO handle null
        }
        return ans;
	}

}