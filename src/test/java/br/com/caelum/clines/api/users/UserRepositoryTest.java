package br.com.caelum.clines.api.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import br.com.caelum.clines.api.aircraft.AircraftRepository;
import br.com.caelum.clines.shared.domain.Aircraft;
import br.com.caelum.clines.shared.domain.AircraftModel;
import br.com.caelum.clines.shared.domain.User;

@DataJpaTest
@TestPropertySource(properties = {"DB_NAME=clines_test", "spring.jpa.hibernate.ddlAuto:create-drop"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {


    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("FULANO", "fulano@gmail.com", "123456");
        entityManager.persist(user);
    }

    @Test
    void shouldReturnUserByEmailWhenExistsAnUserInDB() {

        var optionalUser = repository.findByEmail("fulano@gmail.com");

        assertThat(optionalUser).isPresent();

        var returnedUser = optionalUser.get();

        assertNotNull(returnedUser.getId());
        assertEquals(user.getId(), returnedUser.getId());
        assertEquals(user.getName(), returnedUser.getName());
        assertEquals(user.getPassword(), returnedUser.getPassword());
    }

    @Test
    void shouldReturnAnEmptyOptionalWhenNotExistsUserByEmail() {
        var optionalUser = repository.findByEmail("ciclano@gmail.com");

        assertThat(optionalUser).isEmpty();
    }


    @Test
    void shouldSaveANewUser() {
    	var newUser = new User("FULANO", "fulano@gmail.com", "123456");

        assertNull(newUser.getId());

        repository.save(newUser);

        assertNotNull(newUser.getId());

        assertEquals("FULANO", newUser.getName());
        assertEquals("fulano@gmail.com", newUser.getEmail());
        assertEquals("123456", newUser.getPassword());
    }

}
