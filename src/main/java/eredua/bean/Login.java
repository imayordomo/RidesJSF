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

@Named("login")
@SessionScoped
public class Login implements Serializable {
	private String email;
	private String pasahitza;

	BLFacade facade = FacadeBean.getBusinessLogic();

	public Login() {

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

	public String egiaztatu() {

		if (facade.login(email, pasahitza)) {
			Driver d = facade.getDriver(email);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginUser", d);
			return "Ok";
		}
		else
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wrong mail or password", null));
	        return null;

	}
	public String moveRegister() {
		return "Register";
	}
}