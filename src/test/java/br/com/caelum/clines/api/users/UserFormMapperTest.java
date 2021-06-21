package br.com.caelum.clines.api.users;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import br.com.caelum.clines.shared.domain.User;

class UserFormMapperTest {
	
	private final String NAME = "FULANO";
	private final String EMAIL = "fulano@gmail.com";
	private final String PASSWORD = "123456";
	private UserFormMapper mapper;

	@Test
	void shouldConverterUserFormToUser() {
		var userForm = new UserForm(NAME, EMAIL, PASSWORD);
		mapper = new UserFormMapper();
		
		var user = mapper.map(userForm);
		assertEquals(NAME, user.getName());
		assertEquals(EMAIL, user.getEmail());
		assertEquals(PASSWORD, user.getPassword());
	}

}
