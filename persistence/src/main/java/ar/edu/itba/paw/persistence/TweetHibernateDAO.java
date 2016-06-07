package ar.edu.itba.paw.persistence;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.models.Tweet;
import ar.edu.itba.paw.models.User;

/**
 * Testing model
 */
@Repository
public class TweetHibernateDAO implements TweetDAO{

	@PersistenceContext
	private EntityManager em;

	@Override
	public Tweet create(final String msg, final User owner) {
		Timestamp thisMoment = new Timestamp(new Date().getTime());
        Tweet tweet = new Tweet(msg, owner, thisMoment);
        em.persist(tweet);
		return tweet;
	}

	@Override
	public Tweet retweet(final Tweet tweet, final User user) {
		Timestamp thisMoment = new Timestamp(new Date().getTime());
        Tweet retweet = new Tweet(user, thisMoment, tweet);
        em.persist(retweet);
		return retweet;
	}

	@Override
	public List<Tweet> getTweetsForUser(final User user, final int resultsPerPage, final int page, final User sessionUser) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
		Root<Tweet> tweet = cq.from(Tweet.class);
		cq.where(cb.equal(tweet.get("owner"), user))
			.orderBy(cb.desc(tweet.get("timestamp")));
		List<Tweet> list = em.createQuery(/*"from Tweet as t where t.owner = :user"*/ cq)
				//.setParameter("user", user)
				.setFirstResult((page-1)*resultsPerPage)
				.setMaxResults(resultsPerPage)
				.getResultList();
		return list;
	}

	@Override
	public List<Tweet> getTweetsByHashtag(final String hashtag, final int resultsPerPage, final int page, final User sessionUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tweet> getTweetsByMention(final User user, final int resultsPerPage, final int page, final User sessionUser) {
		@SuppressWarnings("unchecked")
		List<String> mentionIDs = em.createNativeQuery("select tweetID from mentions where userID = ?")
				.setParameter(1, user.getId())
				.getResultList();
		if(mentionIDs.isEmpty())
			return new ArrayList<Tweet>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
		Root<Tweet> root = cq.from(Tweet.class);
		cq.where(cb.in((root.get("id")).in(mentionIDs)))
			.orderBy(cb.desc(root.get("timestamp")));
	
		return em.createQuery(cq)
				.setFirstResult((page-1)*resultsPerPage)
				.setMaxResults(resultsPerPage)
				.getResultList();
	}

	@Override
	public List<Tweet> searchTweets(final String text, final int resultsPerPage, final int page, final User sessionUser) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
		Root<Tweet> root = cq.from(Tweet.class);
		cq.where(cb.like(cb.upper(root.get("msg")), '%'+text.toUpperCase()+'%'))
			.orderBy(cb.desc(root.get("timestamp")));
	
		return em.createQuery(cq)
				.setFirstResult((page-1)*resultsPerPage)
				.setMaxResults(resultsPerPage)
				.getResultList();
	}

	@Override
	public List<Tweet> getGlobalFeed(final int resultsPerPage, final int page, final User sessionUser) {
		return em.createQuery("from Tweet as t order by t.timestamp desc", Tweet.class)
		.setFirstResult((page-1)*resultsPerPage)
		.setMaxResults(resultsPerPage)
		.getResultList();
	}

	@Override
	public List<Tweet> getLogedInFeed(final User user, final int resultsPerPage, final int page) {
		@SuppressWarnings("unchecked")
		List<String> followingIDs = em.createNativeQuery("select followingID from followers where followerID = ?")
				.setParameter(1, user.getId())
				.getResultList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
		Root<Tweet> tweet = cq.from(Tweet.class);
		cq.where(followingIDs.isEmpty()?cb.equal(tweet.get("owner"),user) : cb.or(tweet.get("owner").get("id").in(followingIDs), cb.equal(tweet.get("owner"),user)))  //TODO fix first get
			.orderBy(cb.desc(tweet.get("timestamp")));
	
		return em.createQuery(cq)
				.setFirstResult((page-1)*resultsPerPage)
				.setMaxResults(resultsPerPage)
				.getResultList();
	}

	@Override
	public Integer countTweets(final User user) {
		return ((BigInteger) em.createNativeQuery("select count(aux) from (select * from tweets where tweets.userID = :id) as aux")
				.setParameter("id", user.getId())
				.getSingleResult()).intValue();
	}

	@Override
	public void increaseFavoriteCount(final Tweet tweet) {
		em.createQuery("UPDATE Tweet SET countFavorites = countFavorites+1 WHERE id = :id")
			.setParameter("id", tweet.getId())
			.executeUpdate();
	}

	@Override
	public void decreaseFavoriteCount(final Tweet tweet) {
		em.createQuery("UPDATE Tweet SET countFavorites = countFavorites-1 WHERE id = :id")
		.setParameter("id", tweet.getId())
		.executeUpdate();
	}

	@Override
	public void increaseRetweetCount(final Tweet tweet) {
		em.createQuery("UPDATE Tweet SET countRetweets = countRetweets+1 WHERE id = :id")
		.setParameter("id", tweet.getId())
		.executeUpdate();
	}

	@Override
	public void decreaseRetweetCount(final Tweet tweet) {
		em.createQuery("UPDATE Tweet SET countRetweets = countRetweets-1 WHERE id = :id")
		.setParameter("id", tweet.getId())
		.executeUpdate();
	}

	@Override
	public Tweet getTweetById(final String tweetID, final User sessionUser) {
		return em.createQuery("from Tweet as t where t.id = :tweetID", Tweet.class)
			.setParameter("tweetID", tweetID)
			.getSingleResult();
	}

	@Override
	public Boolean isRetweeted(final Tweet tweet, final User user) {
		return true;
	}

	@Override
	public void unretweet(final Tweet tweet, final User user) {
		em.createNativeQuery("DELETE FROM tweets where retweetFrom = ? and userID = ?")
			.setParameter(1, tweet.getId())
			.setParameter(2, user.getId())
			.executeUpdate();
	}

	@Override
	public List<Tweet> getFavorites(final User user, final int resultsPerPage, final int page, final User sessionUser) {
		@SuppressWarnings("unchecked")
		List<String> favoriteTweetIDs = em.createNativeQuery("select tweetID from favorites where favoriteID = ?")
				.setParameter(1, user.getId()).getResultList();
		if(favoriteTweetIDs.isEmpty())
			return new ArrayList<Tweet>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);
		Root<Tweet> tweet = cq.from(Tweet.class);
		cq.where(tweet.get("id").in(favoriteTweetIDs)) 
			.orderBy(cb.desc(tweet.get("timestamp")));

		return em.createQuery(cq)
				.setFirstResult((page - 1) * resultsPerPage)
				.setMaxResults(resultsPerPage)
				.getResultList();
	}
	
//	private List<Tweet> tweetListQueryWrapper(List<Tweet> tweets, User sessionUser){
//		//List<String> tweetIDs = tweets.stream().map(t->t.getId()).collect(Collectors.toList());
//		
//		StringBuilder builder = new StringBuilder("select tweets.tweetID, max((CASE when favoriteID =? and tweets.tweetID = favorites.tweetID then 1 else 0 end)) as isFavorited, max((CASE when tweets2.retweetFrom = tweets.tweetID and tweets2.userID =? then 1 else 0 end)) as isRetweeted from tweets, tweets as  tweets2, favorites where tweets.tweetID in ");
//		int i;
//		for(i = 0 ; i < tweets.size()-1; i++) 
//		    builder.append(tweets.get(i).getId()).append(", ");
//		builder.append(tweets.get(i).getId()).append(" group by tweets.tweetID");
//		
//		List list = em.createNativeQuery(builder.toString()).getResultList();
//		for(i=0; i<list.size(); i++){
//			
//		};
//		
//	}
	
}
