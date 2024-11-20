package ebook.library.views.access;

import com.vaadin.flow.router.Route;

import ebook.library.data.service.UserService;

import com.vaadin.flow.router.PageTitle;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout {

	private Header h = new Header();
	private LoginForm loginForm = new LoginForm();
	private RegistrationForm registrationForm;

	public LoginView(@Autowired UserService userService) {
		addClassName("login-rich-content");
		// Align form at the center
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		loginForm.getElement().getThemeList().add("light");
		loginForm.setAction("login");
		loginForm.getElement().setAttribute("no-autofocus", "");
		registrationForm = new RegistrationForm(userService, loginForm);
		setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);
		registrationForm.getElement().getThemeList().add("light");
		registrationForm.getElement().setAttribute("no-autofocus", "");
		registrationForm.setVisible(false);
		createButtons();
		add(h, loginForm, registrationForm);
	}

	
	void createButtons(){
		Button signIn = new Button();
		Button signUp = new Button();
		
		signIn.setText("Sign In");
		signIn.setAutofocus(true);
		signIn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		signIn.addClickListener(clickEvent -> {
			if (!loginForm.isVisible()) {
				signUp.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
				registrationForm.setVisible(false);
				
				signIn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				loginForm.setVisible(true);
			}
		});


		signUp.setText("Sign Up");
		signUp.addClickListener(clickEvent -> {
			if (!registrationForm.isVisible()) {
				signIn.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
				loginForm.setVisible(false);
				
				signUp.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				registrationForm.setVisible(true);
			}
		});

		h.add(signIn, signUp);
	}
	
}
