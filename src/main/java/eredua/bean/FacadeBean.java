package eredua.bean;

import java.util.Locale;

import javax.xml.namespace.QName;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import dataAccess.DataAccess;

public class FacadeBean {
	private static FacadeBean singleton = new FacadeBean();
	private static BLFacade facadeInterface;

	private FacadeBean() {
		try {
			facadeInterface = new BLFacadeImplementation(new DataAccess());
		} catch (Exception e) {
			System.out.println("FacadeBean: negozioaren logika sortzean errorea: " + e.getMessage());
		}
	}

	public static BLFacade getBusinessLogic() {
		return facadeInterface;
	}
}
