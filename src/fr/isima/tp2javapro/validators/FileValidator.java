package fr.isima.tp2javapro.validators;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator(value="FileValidator")
public class FileValidator implements Validator{
	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part)value;
		
		if(!file.getContentType().equals("text/csv")
				&& !file.getContentType().equals("application/vnd.ms-excel")){
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur : ", "Veuillez importer un fichier au format .csv."));
		}

		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}
}
