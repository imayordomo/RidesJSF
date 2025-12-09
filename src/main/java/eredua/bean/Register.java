package eredua.bean;

import java.io.Serializable;
import java.util.*;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
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

		if (facade.register(name, email, pasahitza)) {
			FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().put("loginUser", email);
			return "Ok";
		}
		else
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "check the data, something is wrong.", null));
	        return null;

	}
	public String moveLogin() {
		return "Login";
	}
}