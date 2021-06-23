package br.com.caelum.clines.api.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import br.com.caelum.clines.shared.domain.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private static final Long USER_ID = Long.valueOf(1);
	private static final String USER_NAME = "fulano";
	private static final String USER_EMAIL = "fulano@gmail.com";
	private static final String USER_PASSWORD = "123456";
	private final User user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
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
	void shouldCreateNewUser() {
		
		//when
		given(formMapper.map(userForm)).willReturn(user);
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
		given(repository.findById(USER_ID)).willReturn(Optional.of(user));
		given(viewMapper.map(user)).willReturn(userView);

		var userViewResult = service.showUserBy(USER_ID);

		//Verify
		then(repository).should().findById(USER_ID);
		then(viewMapper).should(only()).map(user);
		
		assertEquals(USER_NAME, userViewResult.getName());
		assertEquals(USER_EMAIL, userViewResult.getEmail());
	}


}






















