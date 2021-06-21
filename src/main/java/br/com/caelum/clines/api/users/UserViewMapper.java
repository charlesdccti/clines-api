package br.com.caelum.clines.api.users;

import org.springframework.stereotype.Component;

import br.com.caelum.clines.shared.domain.User;
import br.com.caelum.clines.shared.infra.Mapper;

@Component
public class UserViewMapper implements Mapper<User, UserView> {

    @Override
    public UserView map(User user) {
        return new UserView(user.getName(), user.getEmail());
    }

}
