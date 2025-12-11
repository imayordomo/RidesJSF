package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import businessLogic.BLFacade;
import domain.Car;
import domain.Driver;
import exceptions.CarAlreadyExistException;
import exceptions.RideAlreadyExistException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("addCar")
@SessionScoped
public class AddCar implements Serializable{

    private int numberPlate;
    private int nplaces;
    BLFacade facade =FacadeBean.getBusinessLogic();
    
    private Driver getDriver(){
        return (Driver) FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .get("loginUser");
    }
    
	public int getPlate() {
		return numberPlate;
	}

	public void setPlate(int plate) {
		this.numberPlate = plate;
	}

	public int getSeats() {
		return nplaces;
	}

	public void setSeats(int seats) {
		this.nplaces = seats;
	}
	
	public String backHome() {
		return "home";
	}
	
	public List<Car> getCars() {
        Driver driver = getDriver();
        if(driver != null) {
            return driver.getCars(); 
        }
        return new ArrayList<>();
    }
	
	public String addCar(){
        Driver d = getDriver();
        try {
	        facade.addCar(d.getEmail(), numberPlate, nplaces);
	        d.setCars(facade.getCars(d.getEmail()));
        } catch (CarAlreadyExistException e) {
			System.out.print("kotxea exititzen da jadanik");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Kotxe hori jadanik existitzen da."));
			e.printStackTrace();
		}
        return null;
    }
}
