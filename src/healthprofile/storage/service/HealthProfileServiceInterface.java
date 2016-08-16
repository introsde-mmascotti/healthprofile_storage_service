package healthprofile.storage.service;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import model.Measure;
import model.Person;

@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface HealthProfileServiceInterface {

	@WebMethod
	public Integer createPerson(@WebParam(name="person") Person p);
	
	@WebMethod
	public Person getPersonById(@WebParam(name="id") Integer id);
	
	@WebMethod
	public List<Measure> getHealthProfile(@WebParam(name="person_id") Integer person_id);
	
	@WebMethod
	public Measure getMeasure(
			@WebParam(name="person_id") Integer person_id,
			@WebParam(name="measuretype") String measuretype);
	
	@WebMethod
	public void addMeasureToHealthprofile(
			@WebParam(name="person") Person p,
			@WebParam(name="measure") Measure m );
	
	@WebMethod
	public Measure updateMeasure( @WebParam(name="measure") Measure m );
	
	@WebMethod
	public List<Measure> getMeasureHistory(
		@WebParam(name="person_id") Integer  person_id,
		@WebParam(name="measure") String measuretype, 
		@WebParam(name="from") Calendar from,
		@WebParam(name="to") Calendar to);
}
