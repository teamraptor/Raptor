package ar.edu.itba.paw.webapp.controllers;

import ar.edu.itba.paw.models.Tweet;
import ar.edu.itba.paw.services.TweetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Controller
public class TimelineController {
	
	private static final String MAP_USER = "/user/";
	private static final String MAP_USERS = "/user/{username}";
	private final static String REDIRECT = "redirect:";

	private static final String TIMELINE = "timeline";

	private final static String MAP_TWEET_ACTION = "/tweetAction";
	
	private static final String USERNAME = "username";

	private static final String USER = "user";
	
	private static final String TWEET_LIST = "tweetList";

	private static final String MESSAGE = "message";
	
	@Autowired
	private UserService userService;

	@Autowired
	private TweetService tweetService;

	@RequestMapping(value=MAP_USERS, method= RequestMethod.GET)
	public ModelAndView timeline(@PathVariable(value=USERNAME) String username) {
		final ModelAndView mav = new ModelAndView(TIMELINE);
		User u = userService.getUserWithUsername(username);

		if(u != null){

			mav.addObject(USER, u);

			List<Tweet> tweetList = tweetService.getTimeline(u.getId());

			mav.addObject(TWEET_LIST, tweetList);

		}
		return mav;
	}

	@RequestMapping(value = MAP_TWEET_ACTION, method = RequestMethod.POST)
	public String registerAction(@RequestParam(value=MESSAGE, required=true) String message,
								 @RequestParam(value=USERNAME, required = true) String username) {

		tweetService.register(message, userService.getUserWithUsername(username));

		return REDIRECT + MAP_USER + username;
	}
}
