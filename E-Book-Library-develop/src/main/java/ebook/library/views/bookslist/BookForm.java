package ebook.library.views.bookslist;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBox.ItemFilter;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;

import ebook.library.data.entity.AuthorEntity;
import ebook.library.data.entity.BookEntity;
import ebook.library.data.service.AuthorService;
import ebook.library.data.service.BookService;

public class BookForm extends FormLayout {
	private AuthorService authorService;
	private BookService bookService;
	private BeanValidationBinder<BookEntity> binder = new BeanValidationBinder<>(BookEntity.class);
	private Dialog dialog;
	public BooksListView listView;
	
	
	public BookForm(@Autowired BookService bookService, @Autowired AuthorService authorService, BooksListView listView) {
		this.bookService = bookService;
		this.authorService = authorService;
		this.listView = listView;
	}

	public void openModalForm(BookEntity bookEntity) {
		dialog = new Dialog();
		dialog.setMaxWidth("700px");
		dialog.setModal(true);
		dialog.add(getForm(bookEntity));
		dialog.open();
	}

	public void remove(BookEntity bookEntity) {
		Dialog dialog = new Dialog();

		H3 title = new H3("Deletion Confirmation");

		Div body = new Div();
		body.setText("Are you sure you want to delete " + bookEntity.getTitle() + "?");
		Button confirm = new Button("Confirm", l1 -> {
			bookService.delete(bookEntity.getId());
//			fireEvent(new BookEvent(this, false));
			Notification.show("Book Deleted!").addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			this.listView.resetGrid();
			dialog.close();
		});
		Button cancel = new Button("Cancel", l1 -> dialog.close());
		HorizontalLayout dialogButtons = new HorizontalLayout(confirm, cancel);
		VerticalLayout dialogBody = new VerticalLayout(title, body, dialogButtons);
		dialog.add(dialogBody);
		dialog.open();
	}
	
	private VerticalLayout getForm(BookEntity book) {
		VerticalLayout headerContent = generateHeader(book);	
		HorizontalLayout dialogButtons = generateFooter(book);
		FormLayout formLayout = generateBody(book);
		
		VerticalLayout dialogBody = new VerticalLayout(headerContent, formLayout, dialogButtons);
		dialogBody.setAlignItems(Alignment.CENTER);
		dialogBody.expand(this);
		dialogBody.setSizeFull();

		return dialogBody;
	}

	private HorizontalLayout generateFooter(BookEntity book) {
		HorizontalLayout footer = new HorizontalLayout();
		Button save = new Button("Save", l -> {

			boolean beanIsValid = binder.writeBeanIfValid(book);
			if (beanIsValid) {
				bookService.update(book);
				Notification.show("Successfully saved book!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				fireEvent(new BookEvent(this, false));
				this.listView.resetGrid();
				dialog.close();
			}
		});
		save.getElement().getStyle().set("margin-left", "auto");
		final Button cancel = new Button("Cancel", l1 -> dialog.close());
		footer.setWidthFull();
		footer.add(save, cancel);
		return footer;
	}

	private VerticalLayout generateHeader(BookEntity book) {
		H2 formTitle = new H2(book.getId() != null ? "Edit Book Info" : "Add Book Info");

		VerticalLayout headerContent = new VerticalLayout();
		headerContent.setSizeFull();
		headerContent.setPadding(false);
		headerContent.setSpacing(false);
		headerContent.setMargin(false);
		headerContent.add(formTitle);
		headerContent.setHorizontalComponentAlignment(Alignment.CENTER, formTitle);
		
		return headerContent;
	}
	
	private FormLayout generateBody(BookEntity book) {
		final TextField title = new TextField();
		title.setWidthFull();
		final TextField cover = new TextField();
		cover.setWidthFull();
		final TextField isbn = new TextField();
		isbn.setWidthFull();

		final ComboBox<Integer> yearPublished = new ComboBox("", getSelectableYears());
		yearPublished.setValue(2021);
		yearPublished.setWidthFull();

		final ComboBox<AuthorEntity> authorComboBox = new ComboBox<AuthorEntity>();
		ItemFilter<AuthorEntity> filter = (author, filterString) -> StringUtils
				.containsIgnoreCase(author.getAuthorName(), filterString);
		authorComboBox.setItems(filter, authorService.findAll());
		authorComboBox.setItemLabelGenerator(AuthorEntity::getAuthorName);
		authorComboBox.setWidthFull();

		final TextArea description = new TextArea();
		description.setWidthFull();
		description.setMaxLength(2000);
		description.setValueChangeMode(ValueChangeMode.EAGER);
		description.addValueChangeListener(e -> {
			e.getSource().setHelperText(e.getValue().length() + "/" + 2000);
		});

		binder.bind(title, BookEntity::getTitle, BookEntity::setTitle);
		binder.bind(isbn, BookEntity::getIsbn, BookEntity::setIsbn);
		binder.bind(cover, BookEntity::getCoverLocation, BookEntity::setCoverLocation);
		binder.bind(authorComboBox, BookEntity::getAuthor, BookEntity::setAuthor);
		binder.bind(description, "description");
		binder.bind(yearPublished, "yearPublished");

		binder.readBean(book);
		final FormLayout formLayout = new FormLayout();
		formLayout.addFormItem(title, "Title");
		formLayout.addFormItem(isbn, "ISBN");
		formLayout.addFormItem(cover, "Cover");
		formLayout.addFormItem(yearPublished, "Year Published");
		formLayout.addFormItem(authorComboBox, "Author");
		formLayout.addFormItem(description, "Description");
		// These components always take full width
		formLayout.setColspan(title, 2);
		formLayout.setColspan(description, 2);
		formLayout.setColspan(authorComboBox, 2);
		
		return formLayout;
	}
	
	private List<Integer> getSelectableYears() {
		LocalDate now = LocalDate.now(ZoneId.systemDefault());
		List<Integer> selectableYears = IntStream.range(now.getYear() - 99, now.getYear() + 1).boxed()
				.collect(Collectors.toList());

		return selectableYears;
	}
}
