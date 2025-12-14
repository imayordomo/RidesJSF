package eredua.bean;

import java.io.Serializable;
import java.util.*;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Driver;
import eredua.bean.FacadeBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

@Named("register")
@SessionScoped
public class Register implements Serializable {
	private String name;
	private String email;
	private String pasahitza;

	BLFacade facade = FacadeBean.getBusinessLogic();

	public Register() {

	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	public String erregistratu() {

		String emaitza = facade.register(name, email, pasahitza);
		if (emaitza.equals("SUCCESS")) {
			Driver d = facade.getDriver(email);
	        FacesContext.getCurrentInstance().getExternalContext()
	            .getSessionMap().put("loginUser", d);
			return "Ok";
		}
		else if (emaitza.equals("EMAIL_ALREADY_EXISTS")) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid email", null));
	        
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "check the data, something is wrong.", null));
		}
		return null;
	}
	public String moveLogin() {
		return "Login";
	}
}