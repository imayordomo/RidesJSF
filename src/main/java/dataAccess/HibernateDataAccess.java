package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import eredua.JPAUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class HibernateDataAccess {

	private EntityManager getEntityManager() {
		return JPAUtil.getEntityManager();
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {

		if (!isInitializeDB()) {

			EntityManager db = getEntityManager();

			try {
				db.getTransaction().begin();

				Calendar today = Calendar.getInstance();

				int month = today.get(Calendar.MONTH);
				int year = today.get(Calendar.YEAR);
				if (month == 12) {
					month = 1;
					year += 1;
				}

				// Create drivers
				Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
				Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
				Driver driver3 = new Driver("a", "a", "a");

				// Create rides
				driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
				driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
				driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);

				driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

				driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
				driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
				driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

				driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

				db.persist(driver1);
				db.persist(driver2);
				db.persist(driver3);

				db.getTransaction().commit();
				System.out.println("DB initialized");
			} catch (Exception e) {
				if (db.getTransaction().isActive())
					db.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
	}

	private boolean isInitializeDB() {
		EntityManager db = getEntityManager();
		try {
			Long query = (Long) db.createQuery("SELECT count(r) FROM Ride r").getSingleResult();
			
			if (query > 0) {
				return true;
			} else {
				return false;
			}
		} finally {
			db.close();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		EntityManager db = getEntityManager();
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		db.close();
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		EntityManager db = getEntityManager();
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		db.close();
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);

		if (new Date().compareTo(date) > 0) {
			throw new RideMustBeLaterThanTodayException(
					ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
		}
		EntityManager db = getEntityManager();
		try {
			db.getTransaction().begin();

			Driver driver = db.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			System.out.println("crea addRide");
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			// next instruction can be obviated
			db.persist(driver);
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		} finally {
			db.close();
		}

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);
		EntityManager db = getEntityManager();

		List<Ride> res = new ArrayList<Ride>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",
				Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		db.close();
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<Date>();

		EntityManager db = getEntityManager();
		try {
			Date firstDayMonthDate = UtilDate.firstDayMonth(date);
			Date lastDayMonthDate = UtilDate.lastDayMonth(date);

			TypedQuery<Date> query = db.createQuery(
					"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",
					Date.class);

			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, firstDayMonthDate);
			query.setParameter(4, lastDayMonthDate);
			List<Date> dates = query.getResultList();
			for (Date d : dates) {
				res.add(d);
			}

			return res;
		} finally {
			db.close();
		}
	}
	
	public boolean login(String email, String password) {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        Driver dr = em.find(Driver.class, email);
	        return dr != null && (password.equals(dr.getPassword()));
	    } finally {
	        em.close();
	    }
	}
	
	public boolean register(String name, String email, String password) {
	    EntityManager em = JPAUtil.getEntityManager();

	    try {
	        if (em.find(Driver.class, email) != null) return false;

	        em.getTransaction().begin();

	        Driver d = new Driver();
	        d.setEmail(email);
	        d.setName(name);
	        d.setPassword(password);
	        em.persist(d);
	        em.getTransaction().commit();
	        return true;

	    } catch(Exception ex) {
	        if (em.getTransaction().isActive()) em.getTransaction().rollback();
	        return false;

	    } finally {
	        em.close();
	    }
	}

}
