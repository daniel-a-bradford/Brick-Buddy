package com.web.brickbuddy.webUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.web.brickbuddy.model.Customer;
import com.web.brickbuddy.repository.CustomerRepository;

@Component
public class DataValidation implements Validator {

	String emailRegex ="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	String passwordRegex ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";	
	
	@Autowired
	private CustomerRepository customerRepository;
		
	@Override
	public boolean supports(Class<?> clazz) {		
		return Customer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		
		Customer customer=(Customer) obj;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "size.user.fname");		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "size.user.lname");	
				
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");		
		if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
	    	errors.rejectValue("email", "size.user.unique");
	    }
		       
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordOld", "NotEmpty");
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordNew1", "NotEmpty");
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordNew2", "NotEmpty");
	    
        if (!customer.getPasswordNew1().equals(customer.getPasswordNew2())) {
	    	errors.rejectValue("passwordNew1", "match.user.password2");
	    	errors.rejectValue("passwordNew2", "match.user.password2");
	    }
        		
		if(!customer.getEmail().matches(emailRegex)) { 
			errors.rejectValue("email","size.user.email"); 
		}
		 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (!customer.getPassword().matches(passwordRegex)) {
        	errors.rejectValue("password", "size.user.password");
	    }
		
	}

}
