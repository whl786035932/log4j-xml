package cn.videoworks.cms.dao;

import org.springframework.stereotype.Repository;

import cn.videoworks.cms.entity.ClassificationRule;
import cn.videoworks.commons.dao.hibernate.AdvancedHibernateDao;

@Repository
public class ClassificationRuleDao extends
		AdvancedHibernateDao<ClassificationRule> {
}
