package ebook.library.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

import ebook.library.security.AuthenticatedUser;
import ebook.library.views.authors.AuthorsGridView;
import ebook.library.views.bookslist.BooksListView;
import ebook.library.views.users.UsersGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class MenuItemInfo {

		private String text;
		private String iconClass;
		private Class<? extends Component> view;

		public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
			this.text = text;
			this.iconClass = iconClass;
			this.view = view;
		}

		public String getText() {
			return text;
		}

		public String getIconClass() {
			return iconClass;
		}

		public Class<? extends Component> getView() {
			return view;
		}

	}

	private H1 viewTitle;

	private AuthenticatedUser authenticatedUser;
	private AccessAnnotationChecker accessChecker;

	public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
		this.authenticatedUser = authenticatedUser;
		this.accessChecker = accessChecker;

		setPrimarySection(Section.NAVBAR);
		addToNavbar(true, createHeaderContent());
		addToDrawer(createDrawerContent());
	}

	private Component createHeaderContent() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.addClassName("text-secondary");
		toggle.addThemeVariants(ButtonVariant.LUMO_ICON);
		toggle.getElement().setAttribute("aria-label", "Menu toggle");

		viewTitle = new H1("");
		viewTitle.addClassNames("m-0", "text-l");

		Button logout = new Button("Log out", e -> authenticatedUser.logout());
		logout.setIcon(VaadinIcon.EXIT.create());

		HorizontalLayout header = new HorizontalLayout(toggle, viewTitle, logout);
		header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		header.expand(viewTitle);
		header.setWidth("100%");
		header.addClassNames("py-0", "px-m");

		return header;
	}

	private Component createDrawerContent() {
		H1 appName = new H1("E-Book Library");
		appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

		com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
				createNavigation(), createFooter());
		section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
		return section;
	}

	private Nav createNavigation() {
		Nav nav = new Nav();
		nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
		nav.getElement().setAttribute("aria-labelledby", "views");

		// Wrap the links in a list; improves accessibility
		UnorderedList list = new UnorderedList();
		list.addClassNames("list-none", "m-0", "p-0");
		nav.add(list);

		for (RouterLink link : createLinks()) {
			ListItem item = new ListItem(link);
			list.add(item);
		}
		return nav;
	}

	private List<RouterLink> createLinks() {
		MenuItemInfo[] menuItems = new MenuItemInfo[] {

				new MenuItemInfo("Books", "la la-list", BooksListView.class), //

				new MenuItemInfo("Authors", "la la-columns", AuthorsGridView.class), //

				new MenuItemInfo("Users", "la la-user", UsersGrid.class), //

		};
		List<RouterLink> links = new ArrayList<>();
		for (MenuItemInfo menuItemInfo : menuItems) {
			links.add(createLink(menuItemInfo));

		}
		return links;
	}

	private static RouterLink createLink(MenuItemInfo menuItemInfo) {
		RouterLink link = new RouterLink();
		link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
		link.setRoute(menuItemInfo.getView());

		Span icon = new Span();
		icon.addClassNames("me-s", "text-l");
		if (!menuItemInfo.getIconClass().isEmpty()) {
			icon.addClassNames(menuItemInfo.getIconClass());
		}

		Span text = new Span(menuItemInfo.getText());
		text.addClassNames("font-medium", "text-s");

		link.add(icon, text);
		return link;
	}

	private Footer createFooter() {
		Footer layout = new Footer();
		layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

		return layout;
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		viewTitle.setText(getCurrentPageTitle());
	}

	private String getCurrentPageTitle() {
		PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
		return title == null ? "" : title.value();
	}
}
