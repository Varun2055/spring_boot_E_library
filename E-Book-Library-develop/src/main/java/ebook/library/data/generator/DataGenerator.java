package ebook.library.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;

import ebook.library.data.entity.AuthorEntity;
import ebook.library.data.entity.BookEntity;
import ebook.library.data.entity.RoleEntity;
import ebook.library.data.entity.Roles;
import ebook.library.data.entity.UserEntity;
import ebook.library.data.repositories.AuthorRepository;
import ebook.library.data.repositories.BookRepository;
import ebook.library.data.repositories.RoleRepository;
import ebook.library.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, 
							    	  UserRepository userRepository, 
							    	  RoleRepository roleRepository,
							    	  AuthorRepository authorRepository,
							    	  BookRepository bookRepository) {
    	
    	return args -> {
			Logger logger = LoggerFactory.getLogger(getClass());
			if (userRepository.count() != 0L) {
				logger.info("Using existing database");
				return;
			}
			logger.info("Generating data");
			
			/*
			 * Generate Roles
			 */
			Set<RoleEntity> roles = new HashSet<>();
			RoleEntity adminRole = new RoleEntity();
			adminRole.setCode(Roles.ADMIN.name());
			roles.add(adminRole);
			
			RoleEntity userRole = new RoleEntity();
			userRole.setCode(Roles.USER.name());
			roles.add(userRole);
			roleRepository.saveAll(roles);
			
			/*
			 * Generate Admin user
			 */
			UserEntity admin = new UserEntity();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin"));
			admin.setEmail("admin@admin.com");
			admin.setFirstName("Admin");
			admin.setLastName("Admin");
			admin.setRoles(Stream.of(
					adminRole).collect(Collectors.toSet()));
			userRepository.save(admin);
			
			generateAuthorsAndBooks(authorRepository, bookRepository);
			
			logger.info("Data successfully generated!");
		};
    }
    
    /**
     * 
     */
    private void generateAuthorsAndBooks(AuthorRepository authorRepository, BookRepository bookRepository) {
    	AuthorEntity author1 = new AuthorEntity();
    	author1.setFirstName("Артър");
    	author1.setLastName("Конан Дойл");
    	author1.setBiography("Сър Артър Конан Дойл е роден на 22 май 1859 г. в шотландския град Единбург, "
    			+ "в семейството на Чарлс Алтамънт Дойл — архитект и художник."
    			+ " Той е трето поред от десет деца. Упорит и прилежен студент — такъв е "
    			+ "портретът на Конан Дойл в Единбургския университет 1876—1881, където "
    			+ "следва медицина. Същевременно през октомври 1879 е отпечатан негов "
    			+ "разказ.През 1881 година Конан Дойл получава университетска диплома на бакалавър по медицина (на английски: bachelor of medicine). "
    			+ "Малко известно е, но Артър Конан Дойл по професия е офталмолог. В периода 1882—1890 опитва да започне лекарска практика, "
    			+ "но без особен успех.От 1891 г. Конан Дойл изоставя лекарската професия и основното му занятие става литературата. "
    			+ "Търсейки герой за своите произведения, той си спомня за преподавателя си от университета Джоузеф Бел, който често изумявал "
    			+ "студентите със своята наблюдателност и умението си с помощта на „дедуктивния метод“ да разплита и най-сложните житейски загадки."
    			+ " Така се появява знаменитият Шерлок Холмс, който донася на автора си световна известност.\n"
    			+ "През 1885 г. Конан Дойл се жени за Луиза Хокинс, която страда от туберкулоза и умира през 1906. През 1907 година се жени за Джин Леки. "
    			+ "Има пет деца: две от първата си жена — Мери и Кингсли, и три от втората — Джин, Денис и Адриан.\n"
    			+ "Артър Конан Дойл умира от сърдечен пристъп на 7 юли 1930 г. в своя дом в Кроубъроу (на английски: Crowborough (Съсекс).");
    	author1 = authorRepository.save(author1);
    
    	List<BookEntity> books = new ArrayList<BookEntity>();
    	BookEntity book1 = new BookEntity();
    	book1.setCoverLocation("https://www.ciela.com/media/catalog/product/s/h/sherlok-holms-arthur-conan-doil-infodar.jpg");
    	book1.setTitle("Шерлок Холмс");
    	book1.setIsbn("9786192440350");
    	book1.setYearPublished(2021);
    	book1.setDescription("Подборът и илюстрациите на известния руски художник Антон Ломаев придават уникален облик на сборника и го превръщат "
    			+ "в ценна придобивка и великолепен подарък за всички почитатели на творчеството на сър Артър Конан Дойл.");
    	book1.setAuthor(author1);
    	books.add(book1);
    	
    	BookEntity book2 = new BookEntity();
    	book2.setCoverLocation("https://assets.chitanka.info/thumb/book-cover/05/1344.max.jpg");
    	book2.setTitle("Баскервилското куче");
    	book2.setIsbn("9780140350647");
    	book2.setYearPublished(1902);
    	book2.setDescription("Над рода Баскервил от поколения тегне страшно проклятие. Преданието разказва за призрачно куче с чудовищни размери, "
    			+ "което броди сред обвитото в мъгли тресавище край уединеното фамилно владение в Девън и преследва мъжете от рода. "
    			+ "Мнозина от тях са застигнати от внезапна, ужасна и тайнствена смърт. Когато младият Хенри Баскервил пристига в Англия, "
    			+ "за да наследи семейното имение, след като неговият чичо, сър Чарлс, умира при странни обстоятелства, страховитият звяр се "
    			+ "появява отново. Дали проклятието над рода Баскервил продължава? Или Хенри е жертва на внимателно планиран заговор? "
    			+ "Само Шерлок Холмс може да разреши този дяволски заплетен случай…");
    	book2.setAuthor(author1);
    	books.add(book2);
    	
    	BookEntity book3 = new BookEntity();
    	book3.setCoverLocation("https://assets.chitanka.info/thumb/book-cover/28/10257.250.jpg");
    	book3.setTitle("Сухопътният пират");
    	book3.setIsbn("954-404-023-4");
    	book3.setYearPublished(1992);
    	book3.setDescription("");
    	book3.setAuthor(author1);
    	books.add(book3);
    	
    	bookRepository.saveAll(books);
    }

}