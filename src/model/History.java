package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class History {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private int id;
	
	@XmlElement
	private String type;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("created")
	@XmlElement
	private Set<Measure> measures;
	
	public History(){
		measures = new HashSet<Measure>();
	}
	
	public History (String type){
		measures = new HashSet<Measure>();
		this.type = type;
	}

	public int getId() { return id;	}
	public String getType() { return type; }
	public Set<Measure> getMeasures() { return measures; }

	public void setId(int id) { this.id = id; }
	public void setType(String type) { this.type = type; }
	
	public boolean addMeasure(Measure m) {
		if (m.getType().equals(this.type)) {
			try {
				m = (Measure) m.clone();
				m.setHistory_id(new Integer(this.id));
				return measures.add(m);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}		
		}
		else
			throw new RuntimeException("The measure has type '" + m.getType() + "', but the History has type '" + this.type + "'.");
		return false;
	}
	
	public boolean removeMeasure(Measure m){
		boolean removed = measures.remove(m);
		if (removed)
			m.setHistory_id(null);
		return removed;
	}	
}
