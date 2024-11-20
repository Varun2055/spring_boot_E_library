package ebook.library.views.users;

import org.apache.commons.lang3.StringUtils;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import ebook.library.data.entity.AuthorEntity;
import ebook.library.data.entity.RoleEntity;
import ebook.library.data.entity.UserEntity;
import ebook.library.data.service.RoleService;
import ebook.library.data.service.UserService;
import ebook.library.views.MainLayout;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UsersGrid extends Div {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;
	
	private Collection<UserEntity> users;
	private Grid<UserEntity> grid;
	private ListDataProvider<UserEntity> dataProvider;
	private SplitLayout splitLayout;
	BeanValidationBinder<UserEntity> binder;
	VerticalLayout vLayout;

	private boolean sidebarCollapsed = true;

	public UsersGrid(@Autowired final UserService userService,  @Autowired PasswordEncoder passwordEncoder, @Autowired RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		init();
	}

	private void init() {
		vLayout = new VerticalLayout();
		users = userService.findAll();
		createGrid();

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.add(createUserButton());
		verticalLayout.add(grid);
		verticalLayout.setMinWidth("70%");
		splitLayout = new SplitLayout(verticalLayout, vLayout);
		// Sets the width for the first child to 70%, giving
		// the second child the remaining width of 30%
		updateSidebar();

		add(splitLayout);
	}

	private void createGrid() {
		grid = new Grid<UserEntity>(UserEntity.class, false);
		grid.setMinWidth("70%");

		dataProvider = new ListDataProvider<UserEntity>(users);
		grid.setItems(dataProvider);

		createColumns();

		GridContextMenu<UserEntity> menu = grid.addContextMenu();
		menu.addItem("Edit", event -> {
			Optional<UserEntity> user = event.getItem();
			if (!user.isEmpty()) {
				openUserForm(user.get());
			}
		});
		menu.addItem("Delete", event -> {
			remove();
		});
	}

	private void createColumns() {
		Column<UserEntity> firstNameColumn = grid.addColumn(UserEntity::getFirstName).setHeader("First Name")
				.setSortable(true).setAutoWidth(true);

		Column<UserEntity> lastNameColumn = grid.addColumn(UserEntity::getLastName).setHeader("Last Name")
				.setSortable(true).setAutoWidth(true);

		Column<UserEntity> usernameColumn = grid.addColumn(UserEntity::getUsername).setHeader("Username")
				.setSortable(true).setAutoWidth(true);

		Column<UserEntity> emailColumn = grid.addColumn(UserEntity::getEmail).setHeader("Email").setSortable(true)
				.setAutoWidth(true);

		// first name filter
		TextField firstNameFilter = new TextField();
		firstNameFilter.addValueChangeListener(l -> {
			String value = l.getValue();
			SerializablePredicate<UserEntity> filter = user -> StringUtils.containsIgnoreCase(user.getFirstName(),
					value);
			addFilter(filter);
		});
		firstNameFilter.setWidthFull();

		// last name filter
		TextField lastNameFilter = new TextField();
		lastNameFilter.addValueChangeListener(l -> {
			String value = l.getValue();
			SerializablePredicate<UserEntity> filter = user -> StringUtils.containsIgnoreCase(user.getLastName(),
					value);
			addFilter(filter);
		});
		lastNameFilter.setWidthFull();

		// username filter
		TextField usernameFilter = new TextField();
		usernameFilter.addValueChangeListener(l -> {
			String value = l.getValue();
			SerializablePredicate<UserEntity> filter = user -> StringUtils.containsIgnoreCase(user.getUsername(),
					value);
			addFilter(filter);
		});
		usernameFilter.setWidthFull();

		// email filter
		TextField emailFilter = new TextField();
		emailFilter.setWidthFull();
		emailFilter.addValueChangeListener(l -> {
			String value = l.getValue();
			SerializablePredicate<UserEntity> filter = user -> StringUtils.containsIgnoreCase(user.getEmail(), value);
			addFilter(filter);
		});
		emailFilter.setWidthFull();

		HeaderRow headerRow = grid.appendHeaderRow();
		headerRow.getCell(firstNameColumn).setComponent(firstNameFilter);
		headerRow.getCell(lastNameColumn).setComponent(lastNameFilter);
		headerRow.getCell(usernameColumn).setComponent(usernameFilter);
		headerRow.getCell(emailColumn).setComponent(emailFilter);
	}

	private void addFilter(final SerializablePredicate<UserEntity> filter) {
		dataProvider.clearFilters();
		dataProvider.addFilter(filter);
	}

	private void remove() {
		Dialog dialog = new Dialog();

		H1 title = new H1("Deletion Confirmation");

		Div body = new Div();
		UserEntity user = grid.asSingleSelect().getValue();
		body.setText("Are you sure you want to delete " + user.getFirstName() + " " + user.getLastName() + "?");
		Button confirm = new Button("Confirm", l1 -> {
			userService.delete(user.getId());
			Notification.show("User Deleted!").addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			resetGrid();
			dialog.close();
		});
		Button cancel = new Button("Cancel", l1 -> dialog.close());
		HorizontalLayout dialogButtons = new HorizontalLayout(confirm, cancel);
		VerticalLayout dialogBody = new VerticalLayout(title, body, dialogButtons);
		dialog.add(dialogBody);
		dialog.open();
	}

	private Button createUserButton() {
		Button newUser = new Button("New User", l -> {
			if (sidebarCollapsed) {
				openUserForm(new UserEntity());
			}
		});
		newUser.setIcon(VaadinIcon.USER.create());
		newUser.getStyle().set("float", "right");

		return newUser;
	}

	private void openUserForm(UserEntity newUser) {
		userForm(newUser);
		vLayout.setVisible(true);
		sidebarCollapsed = false;
		updateSidebar();
	}

	private void userForm(UserEntity newUser) {

		FormLayout formLayout = new FormLayout();

		TextField firstName = new TextField();
		TextField lastName = new TextField();
		TextField username = new TextField();
		PasswordField password = new PasswordField();
		EmailField email = new EmailField();
		MultiSelectListBox<RoleEntity> listBox = new MultiSelectListBox<>();
		listBox.setItems(roleService.findAll());
		listBox.setRenderer(new ComponentRenderer<>(role ->
		new Text(role.getCode())));
		
		binder = new BeanValidationBinder<>(UserEntity.class);

		binder.bind(firstName, UserEntity::getFirstName, UserEntity::setFirstName);
		binder.bind(lastName, UserEntity::getLastName, UserEntity::setLastName);
		binder.bind(username, UserEntity::getUsername, UserEntity::setUsername);
		binder.bind(password, UserEntity::getPassword, (u, v) -> u.setPassword(passwordEncoder.encode(v)));
		binder.forField(email).withValidator(new EmailValidator("Invalid email."))
		.bind(UserEntity::getEmail, UserEntity::setEmail);
		binder.bind(listBox, UserEntity::getRoles, UserEntity::setRoles);

		binder.readBean(newUser);

		Header header = new Header();
		header.add(new H3(ObjectUtils.isEmpty(newUser) ? "Create User" : "Edit User"));

		final Button save = new Button("Save", l1 -> {

			boolean beanIsValid = binder.writeBeanIfValid(newUser);
			if (beanIsValid) {
				userService.update(newUser);
				Notification.show("Successfully saved user!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				resetGrid();
				if (!sidebarCollapsed) {
					vLayout.setVisible(false);
					sidebarCollapsed = true;
					updateSidebar();
				}
			}
		});
		Button cancel = new Button("Cancel", l1 -> {
			if (!sidebarCollapsed) {
				vLayout.setVisible(false);
				sidebarCollapsed = true;
				updateSidebar();
			}
		});
		HorizontalLayout dialogButtons = new HorizontalLayout(save, cancel);

		formLayout.addFormItem(firstName, "First Name");
		formLayout.addFormItem(lastName, "Last Name");
		formLayout.addFormItem(email, "Email");
		formLayout.addFormItem(username, "Username");
		formLayout.addFormItem(password, "Password");
		formLayout.addFormItem(listBox, "Roles");

		vLayout.removeAll();
		vLayout.add(header, formLayout, dialogButtons);
	}

	private void updateSidebar() {
		splitLayout.setSplitterPosition(sidebarCollapsed ? 100 : 75);
	}

	private void resetGrid() {
		grid.select(null);
		dataProvider.clearFilters();
		users.clear();
		users.addAll(userService.findAll());
		dataProvider.refreshAll();
	}
}
