package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception para guardar e retornar todas as exceções de dados do formulário de departamento
 * @author Mariana
 *
 */

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){  
		return errors;		
	}
	
	public void addErrors(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
