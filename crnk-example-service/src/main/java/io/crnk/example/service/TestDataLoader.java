package io.crnk.example.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.crnk.core.engine.transaction.TransactionRunner;
import io.crnk.example.service.basic.Location;
import io.crnk.example.service.basic.LocationRepository;
import io.crnk.example.service.basic.Screening;
import io.crnk.example.service.basic.ScreeningRepository;
import io.crnk.example.service.basic.Vote;
import io.crnk.example.service.basic.VoteRepository;
import io.crnk.example.service.jpa.MovieEntity;
import io.crnk.example.service.jpa.PersonEntity;
import io.crnk.example.service.jpa.ScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataLoader {

	private long nextId = 1;

	@Autowired
	private EntityManager em;

	@Autowired
	private TransactionRunner transactionRunner;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private VoteRepository voteRepository;


	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ScreeningRepository screeningRepository;

	@PostConstruct
	public void setup() {
		transactionRunner.doInTransaction(new Callable<Object>() {
			@Override
			public Object call() throws Exception {

				createLocation("zurich");
				createLocation("paris");
				createLocation("london");

				createPerson("Ben Affleck");
				createPerson("Anna Kendrick");
				createPerson("Robert Downey Jr.");
				createPerson("Stan Lee");
				createPerson("Jeff Bridges");
				createPerson("Brad Pitt");
				createPerson("Angelina Jolie");
				createPerson("Leonardo DiCaprio");
				createPerson("Kate Winslet");

				createMovie("The Accountant", 2016, Arrays.asList("Ben Affleck", "Anna Kendrick"));
				MovieEntity movie =
						createMovie("Iron Man", 2008, Arrays.asList("Robert Downey Jr.", "Terrence Howard", "Jeff Bridges"));
				createMovie("Titanic", 1997, Arrays.asList("Leonardo DiCaprio", "Kate Winslet"));
				createMovie("Mr. & Mrs. Smith", 2005, Arrays.asList("Brad Pitt", "Angelina Jolie"));

				createScreening(movie);

				for (int i = 0; i < 10; i++) {
					ScheduleEntity scheduleEntity = new ScheduleEntity();
					scheduleEntity.setId((long) i);
					scheduleEntity.setName("schedule" + i);
					em.persist(scheduleEntity);
				}
				em.flush();

				for (int i = 0; i < 100; i++) {
					Vote vote = new Vote();
					vote.setId(UUID.nameUUIDFromBytes(("vote" + i).getBytes()));
					vote.setCount(i);
					vote.setMovie(movie);
					// bypass slow save
					voteRepository.votes.put(vote.getId(), vote);
				}

				return null;
			}
		});
	}

	private void createScreening(MovieEntity movie) {
		Screening screening = new Screening();
		screening.setId(UUID.randomUUID());
		screening.setMovieId(movie.getId());
		screeningRepository.create(screening);
	}

	@PostConstruct
	public void configureJackson() {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	protected MovieEntity createMovie(String title, int year, List<String> actors) {
		MovieEntity movie = new MovieEntity();
		// generate same id based on title
		movie.setId(UUID.nameUUIDFromBytes(title.getBytes()));
		movie.setName(title);
		movie.setYear(year);
		em.persist(movie);
		return movie;
	}

	protected PersonEntity createPerson(String title) {
		PersonEntity person = new PersonEntity();
		person.setId(UUID.nameUUIDFromBytes(title.getBytes()));
		person.setName(title);
		em.persist(person);
		return person;
	}

	protected Location createLocation(String id) {
		Location location = new Location();
		location.setId(id);
		locationRepository.save(location);
		return location;
	}
}
