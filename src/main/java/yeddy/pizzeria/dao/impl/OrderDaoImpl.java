package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.OrderDao;
import yeddy.pizzeria.dao.OrderProductDao;
import yeddy.pizzeria.dao.StatusDao;
import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Order;
import yeddy.pizzeria.model.dto.Status;
import yeddy.pizzeria.model.dto.User;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {

  private final ConnectionPool connectionPool;

  private final UserDao userDao;

  private final StatusDao statusDao;

  public OrderDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
    DaoFactory daoFactory = DaoFactory.getInstance();
    userDao = daoFactory.getDao(UserDao.class);
    statusDao = daoFactory.getDao(StatusDao.class);
  }

  @Override
  public Optional<Order> findById(long orderId) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select o.* from orders o where o.id = ?"
      );
      statement.setLong(1, orderId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(dtoFromResultSet(resultSet));
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Order> findAll() {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select o.* from orders o order by o.id desc"
      );
      ResultSet resultSet = statement.executeQuery();
      List<Order> orders = new ArrayList<>();
      while (resultSet.next()) {
        orders.add(dtoFromResultSet(resultSet));
      }
      return orders;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Order> findAllByUser(User user) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select o.* from orders o where o.user_id = ? order by o.id desc"
      );
      statement.setLong(1, user.getId());
      ResultSet resultSet = statement.executeQuery();
      List<Order> orders = new ArrayList<>();
      while (resultSet.next()) {
        orders.add(dtoFromResultSet(resultSet));
      }
      return orders;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Order> findAllByStatus(Status status) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select o.* from orders o where o.status_id = ? order by o.id desc"
      );
      statement.setLong(1, status.getId());
      ResultSet resultSet = statement.executeQuery();
      List<Order> orders = new ArrayList<>();
      while (resultSet.next()) {
        orders.add(dtoFromResultSet(resultSet));
      }
      return orders;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void save(Order order) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "insert into orders(user_id, status_id, street, house, apartment) values(?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setLong(1, order.getUser().getId());
      statement.setLong(2, order.getStatus().getId());
      statement.setString(3, order.getStreet());
      statement.setString(4, order.getHouse());
      statement.setString(5, order.getApartment());
      statement.executeUpdate();
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        order.setId(generatedKeys.getLong(1));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void update(Order order) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "update orders set status_id = ? where id = ?"
      );
      statement.setLong(1, order.getStatus().getId());
      statement.setLong(2, order.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private Order dtoFromResultSet(ResultSet resultSet) throws SQLException {
    Order order = new Order();
    order.setId(resultSet.getLong("id"));
    order.setUser(userDao.findById(resultSet.getLong("user_id")).orElse(null));
    order.setStatus(statusDao.findById(resultSet.getLong("status_id")).orElse(null));
    order.setStreet(resultSet.getString("street"));
    order.setHouse(resultSet.getString("house"));
    order.setApartment(resultSet.getString("apartment"));
    return order;
  }
}
