package domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Car {
	@Id
	private int numberPlate;// numberPlate
	private int nplaces;
	@ManyToOne
	@JoinColumn(name="driver_email")
	private Driver driver;
	@OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) //Ez kargatzeko ride guztiak eta ez sortzeko erdiko taula
	private List<Ride> rides = new LinkedList<Ride>();

	public Car(int matricula, int nplaces, Driver d) {
		this.numberPlate = matricula;
		this.nplaces = nplaces;
		this.driver = d;
	}

	public Car() {
		super();
	}

	public int getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(int plate) {
		this.numberPlate = plate;
	}

	public int getNplaces() {
		return nplaces;
	}

	public void setNplaces(int nplaces) {
		this.nplaces = nplaces;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public void removeRide(Ride r) {
		rides.remove(r);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(numberPlate, other.numberPlate) && nplaces == other.nplaces;
	}

	public String toString() {
		return Integer.toString(numberPlate) + " " + Integer.toString(nplaces);
	}

}
