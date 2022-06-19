package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.CartItem;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.User;

import java.util.List;
import java.util.Optional;

public interface CartItemDao extends Dao {

  boolean existsByUserAndProduct(User user, Product product);

  Optional<CartItem> findByUserAndProduct(User user, Product product);

  List<CartItem> findAllByUser(User user);

  void save(CartItem cartItem);

  void update(CartItem cartItem);

  void deleteByUserAndProduct(User user, Product product);

  void deleteByUser(User user);
}
