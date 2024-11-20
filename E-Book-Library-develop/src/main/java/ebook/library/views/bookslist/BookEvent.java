package ebook.library.views.bookslist;

import com.vaadin.flow.component.ComponentEvent;

public class BookEvent extends ComponentEvent<BookForm> {
    public BookEvent(BookForm source, boolean fromClient) {
        super(source, fromClient);
    }
}