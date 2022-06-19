package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.Status;
import yeddy.pizzeria.model.dto.User;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends Dao {

  Optional<Order> findById(long orderId);

  List<Order> findAll();

  List<Order> findAllByUser(User user);

  List<Order> findAllByStatus(Status status);

  void save(Order order);

  void update(Order order);
}
