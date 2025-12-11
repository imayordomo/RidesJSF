package eredua.bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("home")
@SessionScoped
public class Home implements Serializable{

	public String moveCreateRide() {
		return "CreateRide";
	}
	public String moveQueryRides() {
		return "QueryRides";
	}
	public String logout() {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    return "Login";
	}
	public String moveAddCar() {
	    return "AddCar";
	}
}
