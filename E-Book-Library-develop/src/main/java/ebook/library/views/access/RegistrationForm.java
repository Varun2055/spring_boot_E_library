package ebook.library.views.access;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;

import ebook.library.data.entity.UserEntity;
import ebook.library.data.service.UserService;

public class RegistrationForm extends FormLayout {
	Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
	
	private H3 title;

	private TextField firstName;
	private TextField lastName;

	private EmailField email;
	private TextField username;

	private PasswordField password;
	private PasswordField passwordConfirm;

	private Span errorMessageField;

	private Button submitButton;

	public RegistrationForm(@Autowired UserService userService, LoginForm loginForm) {
		addClassName("vaadin-login-form");

		title = new H3("Sign Up");
		firstName = new TextField("First Name");
		lastName = new TextField("Last Lame");
		email = new EmailField("Email");
		username = new TextField("Username");

		password = new PasswordField("Password");
		passwordConfirm = new PasswordField("Confirm password");

		setRequiredIndicatorVisible(firstName, lastName, username, email, password, passwordConfirm);

		errorMessageField = new Span();

		submitButton = new Button("Sign Up");
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitButton.addClickListener(event ->{
			 UserEntity newUser = new UserEntity();
             boolean beanIsValid = binder.writeBeanIfValid(newUser);
 			if (beanIsValid) {
 				userService.update(newUser);
 				setVisible(false);
 				loginForm.setVisible(true);
 				Notification notification = Notification.show("Successfull registration!");
 				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
 			}
		});
		
		
		binder.bind(firstName, UserEntity::getFirstName, UserEntity::setFirstName);
		binder.bind(lastName, UserEntity::getLastName, UserEntity::setLastName);
		binder.bind(username, UserEntity::getUsername, UserEntity::setUsername);

		binder.forField(password)
		.withValidator(pass -> pass != null && pass.equals(passwordConfirm.getValue()), "Passwords do not match.")
		.bind(UserEntity::getPassword, UserEntity::setPassword);
		
		binder.forField(email).withValidator(new EmailValidator("Invalid email."))
		.bind(UserEntity::getEmail, UserEntity::setEmail);
		
		passwordConfirm.addValueChangeListener(e -> {
			binder.validate();
		});

		// Set the label where bean-level error messages go
		binder.setStatusLabel(errorMessageField);

		add(title, firstName, lastName, email, username, password, passwordConfirm, errorMessageField, submitButton);

		// Max width of the Form
		setMaxWidth("500px");

		// Allow the form layout to be responsive.
		// On device widths 0-490px we have one column.
		// Otherwise, we have two columns.
		setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
				new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

		// These components always take full width
		setColspan(title, 2);
		setColspan(email, 2);
		setColspan(username, 2);
		setColspan(errorMessageField, 2);
		setColspan(submitButton, 2);
	}
	
	private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
		Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
	}

}
