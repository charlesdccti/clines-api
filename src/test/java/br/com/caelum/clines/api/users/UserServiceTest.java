package br.com.caelum.clines.api.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import br.com.caelum.clines.shared.domain.Aircraft;
import br.com.caelum.clines.shared.domain.User;
import br.com.caelum.clines.shared.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private static final Long USER_ID = Long.valueOf(1);
	private static final String USER_NAME = "fulano";
	private static final String USER_EMAIL = "fulano@gmail.com";
	private static final String USER_PASSWORD = "123456";
	private static final long NON_EXISTING_USER_ID = 0;
	private final static User DEFAULT_USER = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
	private static final List<User> ALL_USER = List.of(DEFAULT_USER);
	
	private final UserForm userForm = new UserForm(USER_NAME, USER_EMAIL, USER_PASSWORD);
	private final UserView userView = new UserView(USER_NAME, USER_EMAIL);


	@Spy
	private UserFormMapper formMapper;

	@Spy
	private UserViewMapper viewMapper;

	@Mock
	private UserRepository repository;

	@InjectMocks
	private UserService service;


	@Test
	void shouldCreateAnNewUser() {
		
		//when
		given(formMapper.map(userForm)).willReturn(DEFAULT_USER);
		given(repository.findByEmail(userForm.getEmail())).willReturn(Optional.empty());

		var createUserId = service.createUserBy(userForm);

		//verify(repository).save(user);
		then(repository).should().findByEmail(userForm.getEmail());
		then(formMapper).should(only()).map(userForm);
		then(repository).should().save(any());
		assertNotNull(createUserId);
	}


	@Test
	void shouldReturnSingleAnUserViewWhenExistingInRepository() {

		//when
		given(repository.findById(USER_ID)).willReturn(Optional.of(DEFAULT_USER));
		given(viewMapper.map(DEFAULT_USER)).willReturn(userView);

		var userViewResult = service.showUserBy(USER_ID);

		//Verify
		then(repository).should().findById(USER_ID);
		then(viewMapper).should(only()).map(DEFAULT_USER);
		
		assertEquals(USER_NAME, userViewResult.getName());
		assertEquals(USER_EMAIL, userViewResult.getEmail());
	}

    @Test
    void shouldThrowExceptionWhenUserIdNotExistingInRepository() {
        given(repository.findById(NON_EXISTING_USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.showUserBy(NON_EXISTING_USER_ID));

        then(repository).should(only()).findById(NON_EXISTING_USER_ID);
        then(viewMapper).shouldHaveNoInteractions();
        then(formMapper).shouldHaveNoInteractions();
    }
    
    @Test
    void shouldReturnAListOfUserViewForEachUserInRepository() {
        given(repository.findAll()).willReturn(ALL_USER);

        var allUserViews = service.listAllUsers();

        then(repository).should(only()).findAll();
        then(viewMapper).should(only()).map(DEFAULT_USER);
        then(formMapper).shouldHaveNoInteractions();

        assertEquals(ALL_USER.size(), allUserViews.size());

        var userView = allUserViews.get(0);

        assertEquals(USER_NAME, userView.getName());
        assertEquals(USER_EMAIL, userView.getEmail());
    }
    
    @Test
    void shouldReturnAnEmptyListWhenHasNoUserInRepository() {
        given(repository.findAll()).willReturn(List.of());

        var allUserViews = service.listAllUsers();

        assertEquals(0, allUserViews.size());

        then(repository).should(only()).findAll();
        then(viewMapper).shouldHaveNoInteractions();
        then(formMapper).shouldHaveNoInteractions();
    }


}






















