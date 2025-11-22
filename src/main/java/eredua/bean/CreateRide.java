package eredua.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import gui.MainGUI;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("createRide")
@SessionScoped
public class CreateRide implements Serializable{
	private Date data;
	private Date today;
	private String departCity;
	private String arrivalCity;
	private int seats;
	private float price;
	BLFacade facade =FacadeBean.getBusinessLogic();
	
	public CreateRide() {
		
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
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getData() { 
		return data; }
	
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
	public Date getToday() {
	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);
	    return c.getTime();
	}
	
	public void onDateSelect(SelectEvent event) {
		setData((Date)event.getObject());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: "+event.getObject()));
	}
	public String backHome() {
		return "Home";
	}
	
	public void createRide() {
		try {
			Ride r=facade.createRide(departCity, arrivalCity, data, seats, price, "driver1@gmail.com");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ride sortua. ID:" + r.getRideNumber()));
		} catch (RideMustBeLaterThanTodayException e) {
			System.out.print("Bidaia gaurgo egunaren ondoren izan beharko litzake");
			e.printStackTrace();
		} catch (RideAlreadyExistException e) {
			System.out.print("Bidaia exititzen da jadanik");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Bidai hori jadanik existitzen da."));
			e.printStackTrace();
		}
	}
	
	
	
	
}
