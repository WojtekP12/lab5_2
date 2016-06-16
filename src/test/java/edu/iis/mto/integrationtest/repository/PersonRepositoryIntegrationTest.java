package edu.iis.mto.integrationtest.repository;

import static edu.iis.mto.integrationtest.repository.PersonBuilder.person;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import edu.iis.mto.integrationtest.model.Person;

//restore application context to its original state before each test class is run. @DirtiesContext - force reset
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonRepositoryIntegrationTest extends IntegrationTest {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void testCanAccessDbAndGetTestData() {
		List<Person> foundTestPersons = personRepository.findAll();
		assertEquals(2, foundTestPersons.size());
	}

	@Test
	public void testSaveNewPersonAndCheckIsPersisted() {
		long count = personRepository.count();
		personRepository.save(a(person().withId(count + 1).withFirstName("Roberto").withLastName("Mancini")));
		assertEquals(count + 1, personRepository.count());
		assertEquals("Mancini", personRepository.findOne(count + 1).getLastName());
	}
	
	@Test
	public void testUpdatePersonAndCheckTheResult() 
	{
		Person person = personRepository.findOne(1L);
		
		person.setFirstName("Piotr");
		person.setLastName("Nowak");
		
		personRepository.save(person);
		
		assertEquals(2, personRepository.count());
		assertEquals("Piotr", personRepository.findOne(1L).getFirstName());
		assertEquals("Nowak", personRepository.findOne(1L).getLastName());	
	}
	
	@Test
	public void deletePerson()
	{
		personRepository.delete(1L);
		assertEquals(null, personRepository.findOne(1L));
		assertEquals(1, personRepository.count());	
	}
	
	@Test
	public void testFindPeopleByNameLike() 
	{
		List<Person> foundPersons = new ArrayList<Person>();
		foundPersons = personRepository.findByFirstNameLike("Marian");
		assertEquals(2, foundPersons.size());
	}
	
	private Person a(PersonBuilder builder) {
		return builder.build();
	}

}
