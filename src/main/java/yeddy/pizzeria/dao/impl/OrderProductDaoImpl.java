package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.OrderDao;
import yeddy.pizzeria.dao.OrderProductDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.OrderProduct;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderProductDaoImpl implements OrderProductDao {

  private final ConnectionPool connectionPool;

  private final OrderDao orderDao;

  private final ProductDao productDao;

  public OrderProductDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
    DaoFactory daoFactory = DaoFactory.getInstance();
    orderDao = daoFactory.getDao(OrderDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  public List<OrderProduct> findAllByOrder(Order order) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select op.* from order_products op where op.order_id = ?"
      );
      statement.setLong(1, order.getId());
      ResultSet resultSet = statement.executeQuery();
      List<OrderProduct> orderProducts = new ArrayList<>();
      while (resultSet.next()) {
        orderProducts.add(dtoFromResultSet(resultSet));
      }
      return orderProducts;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void save(OrderProduct orderProduct) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "insert into order_products(order_id, product_id, amount) values (?, ?, ?)"
      );
      statement.setLong(1, orderProduct.getOrder().getId());
      statement.setLong(2, orderProduct.getProduct().getId());
      statement.setInt(3, orderProduct.getAmount());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private OrderProduct dtoFromResultSet(ResultSet resultSet) throws SQLException {
    OrderProduct orderProduct = new OrderProduct();
    orderProduct.setOrder(orderDao.findById(resultSet.getLong("order_id")).orElse(null));
    orderProduct.setProduct(productDao.findById(resultSet.getLong("product_id")).orElse(null));
    orderProduct.setAmount(resultSet.getInt("amount"));
    return orderProduct;
  }
}
