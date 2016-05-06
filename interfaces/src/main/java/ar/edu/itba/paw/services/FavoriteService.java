package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

public interface FavoriteService {

	/**
	 * Register a favorite action.
	 *
	 * @param tweetID
	 * @param user
	 */
	void favorite(final String tweetID, final User user);

	/**
	 * Returns whether a user favorited a tweet or not.
	 *
	 * @param tweetID
	 * @param user
	 * @return true if the user favorited the tweet, false if not.
	 */

	Boolean isFavorited(final String tweetID, final User user);

	/**
	 * Stop a favorite action.
	 *
	 * @param tweetID
	 * @param user
	 */
	void unfavorite(final String tweetID, final User user);
}
