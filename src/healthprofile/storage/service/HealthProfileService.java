package healthprofile.storage.service;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.Dao;
import model.Healthprofile;
import model.Measure;
import model.Person;


@WebService
public class HealthProfileService implements HealthProfileServiceInterface {
	
	private static final Logger logger = LogManager.getFormatterLogger("HealthProfileStorageService");

	@Override
	public Integer createPerson(Person p) {
		Integer person_id;
		if (p.getId() == null){
			person_id = Dao.getDao().savePerson(p, false);
			logger.info("Creating " + p.toString());
		}
		else {
			Person existing = Dao.getDao().getPersonById(p.getId());
			existing.setSex(p.getSex());
			existing.setFirstname(p.getFirstname());
			existing.setLastname(p.getLastname());
			existing.setBirthdate(p.getBirthdate());
			person_id = Dao.getDao().savePerson(existing, true);
			logger.info("Updating " + existing.toString());
		}
		
		return person_id;	
	}

	@Override
	public Person getPersonById(Integer id) {
		Person p = Dao.getDao().getPersonById(id);
		return p;
	}

	@Override
	public void addMeasureToHealthprofile(Person p, Measure m) {
		p = Dao.getDao().getPersonById(p.getId());
		if (p.getHeathprofile() == null)
			p.setHeathprofile(new Healthprofile());
		p.getHeathprofile().addMeasure(m);
		Dao.getDao().savePerson(p, true);
		logger.info("Added measure " + m.toString() + " to person " + p.toString());
	}

	@Override
	public List<Measure> getMeasureHistory(Integer person_id, String measuretype, Calendar from, Calendar to) {
		logger.info("Retrieving '%s' history of person with id = %d from %tD to %tD", measuretype, person_id, from, to);
		List<Measure> ret = Dao.getDao().getMeasureHistory(person_id, measuretype, from, to);
		return ret;
	}

	@Override
	public List<Measure> getHealthProfile(Integer person_id) {
		Person p = Dao.getDao().getPersonById(person_id);
		return p.getHeathprofile().getActualMeasures();
	}

	@Override
	public Measure getMeasure(Integer person_id, String measuretype) {
		Person p = Dao.getDao().getPersonById(person_id);
		return p.getHeathprofile().getActualMeasure(measuretype);
	}

	@Override
	public Measure updateMeasure(Measure m) {
		m = Dao.getDao().updateMeasure(m);
		return m;
	}
}
