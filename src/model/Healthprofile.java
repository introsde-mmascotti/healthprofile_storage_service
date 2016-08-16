package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Healthprofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private int id;
	
	@OneToMany(cascade=CascadeType.ALL)
	@MapKey(name="type")
	@XmlTransient
	private Map<String, Measure> measures;
	
	@OneToMany(cascade=CascadeType.ALL)
	@MapKey(name="type")
	@XmlTransient
	private Map<String, History> history;
	
	@OneToOne(mappedBy="healthprofile")
	@XmlTransient
	private Person owner;
	
	public Healthprofile() {
		measures = new HashMap<String, Measure>();
		history = new HashMap<String, History>();
	}
	
	public int getId() { return this.id; }
	public Person getOwner() { return owner; }
	
	public void setId(int id) { this.id = id; }
	public void setOwner(Person owner) { this.owner = owner; }
	
	
	public List<Measure> getActualMeasures(){
		List<Measure> ret = new ArrayList<Measure>(measures.size());
		
		Set<Entry<String, Measure>> set = measures.entrySet();
		
		Iterator<Entry<String, Measure>> it = set.iterator();
		while (it.hasNext()){
			Entry<String, Measure> e = it.next();
			ret.add(e.getValue());
		}		
		return ret;
	}
	
	public List<Measure> getHistory(String type){
		History h = history.get(type);
		if (h != null)
			return new ArrayList<Measure>(h.getMeasures());
		return new ArrayList<Measure>();
	}
	
	public void addMeasure(Measure m){
		m.setPerson(owner);		
		archiveMeasure(m.getType());
		measures.put(m.getType(), m);
	}
	
	public void removeMeasure(Measure m){
		archiveMeasure(m.getType());
		measures.remove(m.getType());
	}
	
	private void archiveMeasure(String type){
		Measure old = measures.get(type);		
		if (old != null){
			History h = history.get(type);
			
			if (h == null){
				h = new History();
				h.setType(type);			
			}
			
			h.addMeasure(old);
			history.put(h.getType(), h);
		}		
		measures.remove(type);
	}
	
	public Measure getActualMeasure(String measuretype){
		return measures.get(measuretype);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Actual measures" + measures.toString() + "\n");
		sb.append("History " + history.toString());
		return sb.toString();
	}
}
	
