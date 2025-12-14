package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.Car;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.CarAlreadyExistException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */

public class BLFacadeImplementation implements BLFacade {

	HibernateDataAccess dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager = new HibernateDataAccess();
		dbManager.initializeDB();
	}

	public BLFacadeImplementation(HibernateDataAccess da) {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager = da;
		dbManager.initializeDB();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getDepartCities() {
		List<String> departLocations = dbManager.getDepartCities();
		return departLocations;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getDestinationCities(String from) {
		List<String> targetCities = dbManager.getArrivalCities(from);
		return targetCities;
	}

	/*

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {

		Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
		return ride;
	};
	*/
	
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail, Car car)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {

		Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail, car);
		return ride;
	};

	/**
	 * {@inheritDoc}
	 */

	public List<Ride> getRides(String from, String to, Date date) {
		List<Ride> rides = dbManager.getRides(from, to, date);
		return rides;
	}

	//Ariketarentzako
	public List<Ride> getRides(Date date) {
		List<Ride> rides = dbManager.getRides(date);
		return rides;
	}
	
	/**
	 * {@inheritDoc}
	 */

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		List<Date> dates = dbManager.getThisMonthDatesWithRides(from, to, date);
		return dates;
	}

	/**
	 * {@inheritDoc}
	 */
	public void initializeBD() {
		dbManager.initializeDB();
	}
	
	public boolean login(String email, String password) {
		return dbManager.login(email,password);
	}
	
	public String register(String izena, String email, String pasahitza) {
		return dbManager.register(izena, email, pasahitza);
	}
	
	@Override
	public Driver getDriver(String email) {
		return dbManager.getDriver(email);
	}
	
	public void addCar(String email, int plate, int seats) throws CarAlreadyExistException{
		dbManager.addCar(email, plate, seats);
	}
	
	public List<Car> getCars(String email){
		return dbManager.getCars(email);
	}
}
