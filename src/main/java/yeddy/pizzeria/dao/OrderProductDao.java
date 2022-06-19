package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.OrderProduct;

import java.util.List;

public interface OrderProductDao extends Dao {

  List<OrderProduct> findAllByOrder(Order order);

  void save(OrderProduct orderProduct);
}
