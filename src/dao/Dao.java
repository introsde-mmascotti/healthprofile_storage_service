package dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Measure;
import model.Person;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dao {
	private static Logger logger;
	
	private static Dao dao = null;
	
	private EntityManager em;
		
	private Dao(String logger_name, String persitence_unit) {
		logger = LogManager.getFormatterLogger(logger_name);
		
		Map<String, String> props = new HashMap<String, String>();
		
		String uri_str = System.getenv("DATABASE_URL");
		URI uri = null;
		try {
			uri = new URI(uri_str);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
				
		String username = uri.getUserInfo().split(":")[0];
		String password = uri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath();
		
		props.put("javax.persistence.jdbc.url", dbUrl);
		props.put("javax.persistence.jdbc.user", username);
		props.put("javax.persistence.jdbc.password", password);
		
		EntityManagerFactory f = Persistence.createEntityManagerFactory(persitence_unit, props);
		
		em = f.createEntityManager();
	}
	
	public static Dao getDao() {
		if (dao == null)
			dao = new Dao("HealthProfileStorageService", "HealthProfileStorageService");
		return dao;
	}
	
	public Integer savePerson(Person p, boolean update){
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		if (update)
			p = em.merge(p);
		else
			em.persist(p);
		tx.commit();
		logger.info("Saved to database " + p.toString() + "\nwith healthprofile:\n" + p.getHeathprofile());
		
		return p.getId();
	}
	
	public void deletePerson(Person p){
		logger.info("Deleting from database " + p.toString());
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.merge(p);
		em.remove(p);
		tx.commit();	
	}
	
	public static Date newDate(int day, int month, int year){
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		return c.getTime();
	}
	
	public Person getPersonById(Integer id) {
		Person p = em.find(Person.class, id);
		
		if (p !=  null)
			logger.info("Retrieving person "  + p.toString());
		else 
			logger.info("No person with id = " + id);
		return p;
	}

	@SuppressWarnings("unchecked")
	public List<Measure> getMeasureHistory(Integer person_id, String measuretype, Calendar from, Calendar to) {
		List<Measure> ret = null;
		String query = "Select m " +
						"from Measure m " +
						"where m.person.id = :pid and m.type = :type and m.created <= :to and m.created >= :from " +
						"order by m.created";
		Query q = em.createQuery(query);
		q.setParameter("pid", person_id);
		q.setParameter("type", measuretype);
		q.setParameter("to", to.getTime());
		q.setParameter("from", from.getTime());
		
		ret =  q.getResultList();
		
		logger.debug("%d measures found.", ret.size());
		logger.trace(ret);
		
		return ret;
	}

	public Measure updateMeasure(Measure m) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		m = em.merge(m);
		tx.commit();
		logger.info("Updated measure: " + m.toString());
		
		return m;
		
	}
}