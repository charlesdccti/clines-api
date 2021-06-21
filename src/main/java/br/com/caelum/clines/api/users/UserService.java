package br.com.caelum.clines.api.users;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import br.com.caelum.clines.api.aircraft.AircraftFormMapper;
import br.com.caelum.clines.shared.exceptions.ResourceAlreadyExistsException;
import br.com.caelum.clines.shared.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository repository;
	private final UserViewMapper viewMapper;
	private final UserFormMapper formMapper;

	public List<UserView> listAllUsers() {
		return repository.findAll().stream().map(viewMapper::map).collect(toList());
	}

	public Long createUserBy(@Valid UserForm form) {

		repository.findByEmail(form.getEmail()).ifPresent(user -> {
			throw new ResourceAlreadyExistsException("User already exists");
		});

		var user = formMapper.map(form);
		
		this.repository.save(user);

		return user.getId();
	}

	public UserView showUserBy(long id) {
		
        return repository.findById(id)
                .map(viewMapper::map)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cannot find User")
                );
	}

}
