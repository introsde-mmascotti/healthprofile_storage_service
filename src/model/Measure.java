package model;

import java.io.Serializable;
import java.lang.Double;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Measure implements Serializable {
	private static final long serialVersionUID = 3588502352471698079L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@XmlTransient
	private Integer history_id;
	
	@XmlTransient
	private Person person;
	
	@XmlElement
	private String type;
	
	@XmlElement
	private String unit;
	
	@XmlElement
	private Double value;
		
	@Temporal(TemporalType.DATE)
	@XmlElement
	private Date created;

	public Measure() {
		super();
	}
	
	public int getId() { return this.id; }
	public Integer getHistory_id() { return history_id;	}
	public Person getPerson() { return person; }
	public String getType() { return this.type; }
	public String getUnit() { return unit; }
	public Double getValue() { return this.value; }
	public Date getCreated() { return this.created; }
	
	public void setId(int id) { this.id = id; }
	public void setHistory_id(Integer history_id) { this.history_id = history_id; }
	public void setPerson(Person person) { this.person = person; }
	public void setType(String type) { this.type = type;}
	public void setUnit(String unit) { this.unit = unit; }
	public void setValue(Double value) { this.value = value; }   
	public void setCreated(Date created) { this.created = created; }
	
	@Override
	public String toString() {
		return String.format("[%d] %s %.2f (%tD)", id, type, value, created);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Measure m = new Measure();
		m.setHistory_id(history_id);
		m.setType(type);
		m.setUnit(unit);
		m.setValue(value);
		m.setCreated(created);
		m.setPerson(person);
		return m;
	}
}
