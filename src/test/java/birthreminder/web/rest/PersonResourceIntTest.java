package birthreminder.web.rest;

import birthreminder.Application;
import birthreminder.domain.Person;
import birthreminder.repository.PersonRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DAY = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    private static final Boolean DEFAULT_REMIND = false;
    private static final Boolean UPDATED_REMIND = true;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonMockMvc;

    private Person person;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonResource personResource = new PersonResource();
        ReflectionTestUtils.setField(personResource, "personRepository", personRepository);
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personRepository.deleteAll();
        person = new Person();
        person.setName(DEFAULT_NAME);
        person.setLastName(DEFAULT_LAST_NAME);
        person.setBirthDay(DEFAULT_BIRTH_DAY);
        person.setInfo(DEFAULT_INFO);
        person.setRemind(DEFAULT_REMIND);
    }

    @Test
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person

        restPersonMockMvc.perform(post("/api/persons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(person)))
                .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> persons = personRepository.findAll();
        assertThat(persons).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = persons.get(persons.size() - 1);
        assertThat(testPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getBirthDay()).isEqualTo(DEFAULT_BIRTH_DAY);
        assertThat(testPerson.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testPerson.getRemind()).isEqualTo(DEFAULT_REMIND);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setName(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/persons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(person)))
                .andExpect(status().isBadRequest());

        List<Person> persons = personRepository.findAll();
        assertThat(persons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setLastName(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/persons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(person)))
                .andExpect(status().isBadRequest());

        List<Person> persons = personRepository.findAll();
        assertThat(persons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPersons() throws Exception {
        // Initialize the database
        personRepository.save(person);

        // Get all the persons
        restPersonMockMvc.perform(get("/api/persons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
                .andExpect(jsonPath("$.[*].remind").value(hasItem(DEFAULT_REMIND.booleanValue())));
    }

    @Test
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.save(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/persons/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(person.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.birthDay").value(DEFAULT_BIRTH_DAY.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.remind").value(DEFAULT_REMIND.booleanValue()));
    }

    @Test
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/persons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePerson() throws Exception {
        // Initialize the database
        personRepository.save(person);

		int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        person.setName(UPDATED_NAME);
        person.setLastName(UPDATED_LAST_NAME);
        person.setBirthDay(UPDATED_BIRTH_DAY);
        person.setInfo(UPDATED_INFO);
        person.setRemind(UPDATED_REMIND);

        restPersonMockMvc.perform(put("/api/persons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(person)))
                .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> persons = personRepository.findAll();
        assertThat(persons).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = persons.get(persons.size() - 1);
        assertThat(testPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
        assertThat(testPerson.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testPerson.getRemind()).isEqualTo(UPDATED_REMIND);
    }

    @Test
    public void deletePerson() throws Exception {
        // Initialize the database
        personRepository.save(person);

		int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Get the person
        restPersonMockMvc.perform(delete("/api/persons/{id}", person.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Person> persons = personRepository.findAll();
        assertThat(persons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
