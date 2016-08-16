package model;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import model.Healthprofile;


@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static char FEMALE = 'f';
	public final static char MALE = 'm';
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute
	private Integer id;
	
	@XmlElement
	private char sex;
	
	@XmlElement
	private String firstname;
	
	@XmlElement
	private String lastname;
	
	@XmlElement
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	@OneToOne(cascade=CascadeType.ALL)
	@XmlTransient
	private Healthprofile healthprofile;

	public Person() {
		super();
		healthprofile = new Healthprofile();
		healthprofile.setOwner(this);
	}
	
	public Integer getId() { return this.id; }
	public char getSex() { return sex; }
	public String getFirstname() { return this.firstname; }
	public String getLastname() { return this.lastname; }
	public Date getBirthdate() { return this.birthdate; }
	public Healthprofile getHeathprofile() { return this.healthprofile; }
	
	public void setId(Integer id) { this.id = id; }
	public void setSex(char sex) { this.sex = sex; }
	public void setFirstname(String firstname) { this.firstname = firstname; }  
	public void setLastname(String lastname) { this.lastname = lastname; }   
	public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }   
	public void setHeathprofile(Healthprofile heathprofile) { this.healthprofile = heathprofile;	}
	
	@Override
	public String toString() {
		return String.format("[%d] %s %s (%c), %tD", id, firstname, lastname, sex, birthdate);
	}
}
