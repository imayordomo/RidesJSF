package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Ride;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("queryRides")
@ViewScoped
public class QueryRides implements Serializable{
	
	private List<Ride> rideslist; 
	private Date data;
	private Ride ride;
	private List<String> departCityList = new ArrayList<String>();
	private String departCity; 
	private List<String> arrivalCityList = new ArrayList<String>();
	private String arrivalCity; 
	
	private BLFacade facade = FacadeBean.getBusinessLogic();
	
	public QueryRides() {
		departCityList = facade.getDepartCities();
	}

	public List<String> getDepartCityList() {
		return departCityList;
	}

	public void setDepartCityList(List<String> departCityList) {
		this.departCityList = departCityList;
	}

	public List<String> getArrivalCityList() {
		return arrivalCityList;
	}

	public void setArrivalCityList(List<String> arrivalCityList) {
		this.arrivalCityList = arrivalCityList;
	}

	public String getDepartCity() {
		return departCity;
	}

	public void setDepartCity(String departCity) {
		this.departCity = departCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
		System.out.println(arrivalCity);
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

    public List<Ride> getRideslist() {
        return rideslist;
    }

    public void setRideslist(List<Ride> rideslist) {
        this.rideslist = rideslist;
    }
    public Date getData() { return data; }
	
	public void setData(Date data) { 
		if (data != null) {
			Calendar cale = Calendar.getInstance();
			cale.setTime(data);
			cale.set(Calendar.HOUR_OF_DAY, 0);
			cale.set(Calendar.MINUTE, 0);
			cale.set(Calendar.SECOND, 0);
			cale.set(Calendar.MILLISECOND, 0);
			this.data = cale.getTime();
		} else {
			this.data = null;
		}
	}
    
	public String backHome() {
		return "Home";
	}
	
	public void onDateSelect(SelectEvent event) {
		//this.data = (Date) event.getObject();
		this.reloadRidesTable();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: "+event.getObject()));
		System.out.println(departCity+' '+arrivalCity+' '+data);
	}
	
	public void onDepartCityChange() {
	    if (departCity != null) {
	        arrivalCityList = facade.getDestinationCities(departCity); 
	    } else {
	        arrivalCityList = new ArrayList<>();
	    }
	    arrivalCity = null; // reset seleccion
	    this.reloadRidesTable();
	}
	
	public void reloadRidesTable() {
		if (departCity != null && arrivalCity != null && data != null) {
	        
	        rideslist = facade.getRides(departCity, arrivalCity, data);
	    } else {
	        rideslist = new ArrayList<>();
	    }
	}
	
}
