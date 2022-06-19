package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.User;

import java.util.Optional;

public interface UserDao extends Dao {

  Optional<User> findById(long id);

  Optional<User> findByLogin(String login);

  boolean existsByLogin(String login);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  void save(User user);

  void update(User user);

  void deleteById(long id);
}
