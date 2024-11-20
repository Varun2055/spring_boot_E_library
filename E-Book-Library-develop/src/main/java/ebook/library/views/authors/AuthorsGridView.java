package ebook.library.views.authors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import ebook.library.data.entity.AuthorEntity;
import ebook.library.data.entity.UserEntity;
import ebook.library.data.service.AuthorService;
import ebook.library.views.MainLayout;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Authors")
@Route(value = "authors", layout = MainLayout.class)
public class AuthorsGridView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private AuthorService authorService;
	private Collection<AuthorEntity> authors;
	private Grid<AuthorEntity> grid;
	private ListDataProvider<AuthorEntity> dataProvider;
	private Editor<AuthorEntity> editor;
	private Column<AuthorEntity> firstNameColumn;
	private List<Component> validationMessages;

	public AuthorsGridView(@Autowired AuthorService authorService) {
		this.authorService = authorService;
		init();
	}

	private void init() {
		authors = authorService.findAll();
		createGrid();
	}

	private void createGrid() {
		grid = new Grid<AuthorEntity>(AuthorEntity.class, false);
		grid.setWidth("100%");

		dataProvider = new ListDataProvider<AuthorEntity>(authors);
		grid.setItems(dataProvider);
		createColumns();
	}

	private void createColumns() {
		firstNameColumn = grid.addColumn(AuthorEntity::getFirstName).setHeader("First Name").setSortable(true)
				.setAutoWidth(true);

		Column<AuthorEntity> lastNameColumn = grid.addColumn(AuthorEntity::getLastName).setHeader("Last Name")
				.setSortable(true).setAutoWidth(true);

		Column<AuthorEntity> biographyColumn = grid.addColumn(AuthorEntity::getBiography).setHeader("Biography").setWidth("70%");
		Column<AuthorEntity> deleteColumn = grid.addComponentColumn(author -> {
		    Button delete = new Button("");
		    delete.setIcon(VaadinIcon.TRASH.create());
		    delete.addClickListener(e -> {
		    	remove(author);
		    	Notification.show("Author deleted!").addThemeVariants(NotificationVariant.LUMO_CONTRAST);
		    });
		    return delete;
		}).setWidth("100px").setFlexGrow(0);
		
		inlineEdit(firstNameColumn, lastNameColumn, biographyColumn);
		addFilter();
	}

	private void inlineEdit(Column<AuthorEntity> firstNameColumn, Column<AuthorEntity> lastNameColumn,
			Column<AuthorEntity> biographyColumn) {

		ValidationMessage firstNameValidationMessage = new ValidationMessage();
		ValidationMessage lastNameValidationMessage = new ValidationMessage();

		Binder<AuthorEntity> binder = new Binder<AuthorEntity>(AuthorEntity.class);
		editor = grid.getEditor();
		
		TextField firstNameField = new TextField();
		firstNameField.setWidthFull();
		addCloseHandler(firstNameField, editor);
		binder.forField(firstNameField).asRequired("First name must not be empty")
				.withStatusLabel(firstNameValidationMessage)
				.bind(AuthorEntity::getFirstName, AuthorEntity::setFirstName);
		firstNameColumn.setEditorComponent(firstNameField);

		TextField lastNameField = new TextField();
		lastNameField.setWidthFull();
		addCloseHandler(lastNameField, editor);
		binder.forField(lastNameField).asRequired("Last name must not be empty")
				.withStatusLabel(lastNameValidationMessage).bind(AuthorEntity::getLastName, AuthorEntity::setLastName);
		lastNameColumn.setEditorComponent(lastNameField);

		TextArea biographyField = new TextArea();
		biographyField.setWidthFull();
		addCloseHandler(biographyField, editor);
		binder.forField(biographyField).bind(AuthorEntity::getBiography, AuthorEntity::setBiography);
		biographyColumn.setEditorComponent(biographyField);
		editor.setBinder(binder);

		grid.addItemDoubleClickListener(e -> editSelectedItem(e.getItem(), e.getColumn()));

		editor.addCancelListener(e -> {
			firstNameValidationMessage.setText("");
			lastNameValidationMessage.setText("");
		});

		getThemeList().clear();
		getThemeList().add("spacing-s");

		validationMessages = new ArrayList<Component>();
		validationMessages.add(firstNameValidationMessage);
		validationMessages.add(lastNameValidationMessage);
	}

	private void editSelectedItem(AuthorEntity author, Column<AuthorEntity> column) {
		editor.editItem(author);
		Component editorComponent = column.getEditorComponent();
		if (editorComponent instanceof Focusable) {
			((Focusable) editorComponent).focus();
		}
	}

	private void addCloseHandler(Component textField, Editor<AuthorEntity> editor) {
		textField.getElement().addEventListener("keydown", e -> {
			editor.cancel();
			resetGrid();
			}).setFilter("event.code === 'Escape'");
		
		textField.getElement().addEventListener("keydown", e -> {
			System.out.println("In filed event listener");
			AuthorEntity author = editor.getItem();
			System.out.println("author: " + author.getLastName());
			System.out.println("author: " + author.getBiography());
			boolean beanIsValid = editor.getBinder().writeBeanIfValid(author);
			System.out.println("beanIsValid: " + beanIsValid);
			if (beanIsValid) {
				authorService.update(author);	
				Notification.show("Successfully saved author!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				editor.closeEditor();
				resetGrid();
			}
		}).setFilter("event.code === 'Enter'");
	}

	private void addFilter() {
		TextField searchField = new TextField();
		searchField.setWidth("50%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
		searchField.addValueChangeListener(l -> {
			final String value = l.getValue();
			final SerializablePredicate<AuthorEntity> filter = author -> StringUtils
					.containsIgnoreCase(author.getAuthorName(), value);
			addFilter(filter);
		});

		Button addNewAuthorBtn = new Button("Add Author");
		addNewAuthorBtn.setIcon(VaadinIcon.USER_CARD.create());
		addNewAuthorBtn.addClickListener(l -> {
			resetGrid();
			AuthorEntity newAuthor = new AuthorEntity();
			dataProvider.getItems().add(newAuthor);
			editSelectedItem(newAuthor, firstNameColumn);
		});

		HorizontalLayout horizontalLayout = new HorizontalLayout(searchField, addNewAuthorBtn);
		VerticalLayout layout = new VerticalLayout(horizontalLayout, grid);
		validationMessages.forEach(msg -> layout.add(msg));
		layout.setPadding(false);
		add(layout);
	}

	private void addFilter(final SerializablePredicate<AuthorEntity> filter) {
		dataProvider.clearFilters();
		dataProvider.addFilter(filter);
	}

	private void resetGrid() {
		authors.clear();
		authors.addAll(authorService.findAll());
		grid.select(null);
		dataProvider.clearFilters();
		dataProvider.refreshAll(); 
	}

	private void remove(AuthorEntity author) {
		Dialog dialog = new Dialog();

		H1 title = new H1("Deletion Confirmation");

		Div body = new Div();
		body.setText("Are you sure you want to delete " + author.getAuthorName() + "?");
		Button confirm = new Button("Confirm", l1 -> {
			authorService.delete(author.getId());
			resetGrid();
			dialog.close();
		});
		Button cancel = new Button("Cancel", l1 -> dialog.close());
		HorizontalLayout dialogButtons = new HorizontalLayout(confirm, cancel);
		VerticalLayout dialogBody = new VerticalLayout(title, body, dialogButtons);
		dialog.add(dialogBody);
		dialog.open();
	}

}
