package com.zzy.service.impl;

import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.dao.BaseDao;
import com.zzy.service.UtilService;

@Transactional
@Service
public class UtilServiceImpl implements UtilService {

	@Autowired
	private BaseDao basedao;

//	public BaseDao getBasedao() {
//		return basedao;
//	}
//
//	public void setBasedao(BaseDao basedao) {
//		this.basedao = basedao;
//	}


	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	public Session getSession() {  
        //事务必须是开启的，否则获取不到  
        return sessionFactory.getCurrentSession();  
    }  
	
	public String getSysID() {
		Session session = this.getCurrentSession();
		//Transaction tx = session.beginTransaction();//只是查询系统ID
		String sysid = session.createSQLQuery(" SELECT REPLACE(UUID(),'-','')  FROM DUAL; ").uniqueResult().toString();
		//tx.commit();
		//session.close();关闭session就不能获取到ID
		return sysid;
	}

	public Session gethibernatesession() {
		return getSessionFactory().openSession();
	}






	public void close(Session session) {
		if(session!=null){
			session.flush();
			session.close();
		}
		
	}

	public int gettotal(Criteria criteria) {
		int total = 0;
		if(criteria!=null){
			criteria.setProjection(Projections.rowCount());
			total = Integer.valueOf(criteria.uniqueResult().toString());
		}
		return total;
	}


	public List<Object[]> getListBySql(String sql) {
		return basedao.getListSql(sql);
	}

	public void executeSql(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);    
        query.executeUpdate();
	}


}
