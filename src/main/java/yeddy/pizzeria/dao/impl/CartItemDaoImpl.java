package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.CartItemDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.CartItem;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.User;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartItemDaoImpl implements CartItemDao {

  private final ConnectionPool connectionPool;

  private final UserDao userDao;

  private final ProductDao productDao;

  public CartItemDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
    DaoFactory daoFactory = DaoFactory.getInstance();
    userDao = daoFactory.getDao(UserDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  public boolean existsByUserAndProduct(User user, Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select count(ci.*) from cart_items ci where ci.user_id = ? and ci.product_id = ?"
      );
      statement.setLong(1, user.getId());
      statement.setLong(2, product.getId());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getLong(1) != 0;
      }
      return false;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public Optional<CartItem> findByUserAndProduct(User user, Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select ci.* from cart_items ci where ci.user_id = ? and ci.product_id = ? limit 1"
      );
      statement.setLong(1, user.getId());
      statement.setLong(2, product.getId());
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
  public List<CartItem> findAllByUser(User user) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select ci.* from cart_items ci where ci.user_id = ?"
      );
      statement.setLong(1, user.getId());
      ResultSet resultSet = statement.executeQuery();
      List<CartItem> cartItems = new ArrayList<>();
      while (resultSet.next()) {
        cartItems.add(dtoFromResultSet(resultSet));
      }
      return cartItems;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void save(CartItem cartItem) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "insert into cart_items(user_id, product_id, amount) values(?, ?, ?)"
      );
      statement.setLong(1, cartItem.getUser().getId());
      statement.setLong(2, cartItem.getProduct().getId());
      statement.setLong(3, cartItem.getAmount());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void update(CartItem cartItem) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "update cart_items set amount = ? where user_id = ? and product_id = ?"
      );
      statement.setInt(1, cartItem.getAmount());
      statement.setLong(2, cartItem.getUser().getId());
      statement.setLong(3, cartItem.getProduct().getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void deleteByUserAndProduct(User user, Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "delete from cart_items where user_id = ? and product_id = ?"
      );
      statement.setLong(1, user.getId());
      statement.setLong(2, product.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void deleteByUser(User user) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "delete from cart_items where user_id = ?"
      );
      statement.setLong(1, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private CartItem dtoFromResultSet(ResultSet resultSet) throws SQLException {
    CartItem cartItem = new CartItem();
    cartItem.setUser(userDao.findById(resultSet.getLong("user_id")).orElse(null));
    cartItem.setProduct(productDao.findById(resultSet.getLong("product_id")).orElse(null));
    cartItem.setAmount(resultSet.getInt("amount"));
    return cartItem;
  }
}
