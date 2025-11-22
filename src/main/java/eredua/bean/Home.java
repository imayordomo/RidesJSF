package eredua.bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("home")
@ApplicationScoped
public class Home implements Serializable{

	public String moveCreateRide() {
		return "CreateRide";
	}
	public String moveQueryRides() {
		return "QueryRides";
	}
	
}
