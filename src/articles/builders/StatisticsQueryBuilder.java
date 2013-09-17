package articles.builders;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import articles.model.UserActivity;
import articles.model.UserStatistics;

public class StatisticsQueryBuilder {
	private CriteriaBuilder cb;
	private CriteriaQuery<UserStatistics> cq;
	private Root<UserStatistics> root;
	private Predicate p;
	private EntityManager em;

	public StatisticsQueryBuilder(EntityManager em) {
		this.em = em;
		this.cb = this.em.getCriteriaBuilder();
		this.cq = this.cb.createQuery(UserStatistics.class);
		this.root = this.cq.from(UserStatistics.class);
		this.p = null;
	}

	public StatisticsQueryBuilder filterByActivity(UserActivity activity) {
		this.p = (this.p == null) ? cb.and(cb.equal(root.get("userActivity"),
				activity)) : cb.and(
				cb.equal(root.get("userActivity"), activity), this.p);
		return this;
	}

	public StatisticsQueryBuilder filterByDate(Date date) {
		this.p = (this.p == null) ? cb.and(cb.between(
				root.<Date> get("activityDate"), date, new Date(date.getTime()
						+ (24 * 3600 * 1000) - 1))) : cb.and(
				cb.between(root.<Date> get("activityDate"), date,
						new Date(date.getTime() + (24 * 3600 * 1000) - 1)), p);
		return this;
	}

	public StatisticsQueryBuilder filterByUserId(int userId) {
		this.p = (this.p == null) ? cb
				.and(cb.equal(root.get("userId"), userId)) : cb.and(
				cb.equal(root.get("userId"), userId), this.p);
		return this;
	}

	public Query build() {
		if (this.p != null)
			cq = cq.where(p);
		return this.em.createQuery(cq);
	}
}
