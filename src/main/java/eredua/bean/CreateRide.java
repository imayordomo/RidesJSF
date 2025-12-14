package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Car;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
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
	private String numberPlate;
	private List<String> carPlateList = new ArrayList<String>();
	BLFacade facade =FacadeBean.getBusinessLogic();
	private Car car;
	
	private Driver getDriver(){
        return (Driver) FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .get("loginUser");
    }
	
	public CreateRide() {
		
	}
	
	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
		Driver driver = getDriver();
	    if (driver == null) {
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No user logged in", null));
	    }
		List<Car> cars = driver.getCars();
		for (Car c : cars) {
			if(String.valueOf(c.getNumberPlate()).equals(numberPlate)) {
				seats = c.getNplaces();
				car = c;
			}
			break;
		}
	}


	public void setCarPlateList(List<String> carPlateList) {
		this.carPlateList = carPlateList;
	}
	
	public List<String> getCarPlateList(){
		Driver driver = getDriver();
	    if (driver == null) {
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No user logged in", null));
	        return null;
	    }
	    
	    List<Car> cars = driver.getCars();
	    List<String> plateList= new ArrayList<String>();
	    for (Car c : cars) {
	    	plateList.add(String.valueOf(c.getNumberPlate()));
	    }
	    carPlateList = plateList;
	    return plateList;
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
		Driver driver = getDriver();
	    if (driver == null) {
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No user logged in", null));
	        return;
	    }
	    if (numberPlate.equals("")) {
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No car selected. If you don't have any, add it on the home menu.", null));
	        return;
	    }
		try {
			
			Ride r=facade.createRide(departCity, arrivalCity, data, seats, price, driver.getEmail(), car);
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
