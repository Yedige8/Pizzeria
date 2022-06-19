package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.RoleDao;
import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.User;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.*;
import java.util.Arrays;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

  private final ConnectionPool connectionPool;

  private final RoleDao roleDao;

  public UserDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
    DaoFactory daoFactory = DaoFactory.getInstance();
    roleDao = daoFactory.getDao(RoleDao.class);
  }

  @Override
  public Optional<User> findById(long id) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select * from users where id = ? limit 1"
      );
      statement.setLong(1, id);
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
  public Optional<User> findByLogin(String login) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select * from users where login = ? limit 1"
      );
      statement.setString(1, login);
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
  public boolean existsByLogin(String login) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select count(u.*) from users u where u.login = ?"
      );
      statement.setString(1, login);
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
  public boolean existsByEmail(String email) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select count(u.*) from users u where u.email = ?"
      );
      statement.setString(1, email);
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
  public boolean existsByPhone(String phone) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select count(u.*) from users u where u.phone = ?"
      );
      statement.setString(1, phone);
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
  public void save(User user) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "insert into users(role_id, login, password, email, phone, first_name, last_name) values(?, ?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setLong(1, user.getRole().getId());
      statement.setString(2, user.getLogin());
      statement.setString(3, user.getPassword());
      statement.setString(4, user.getEmail());
      statement.setString(5, user.getPhone());
      statement.setString(6, user.getFirstName());
      statement.setString(7, user.getLastName());
      statement.executeUpdate();
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        user.setId(generatedKeys.getLong(1));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void update(User user) {

  }

  @Override
  public void deleteById(long id) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "delete from users where id = ?"
      );
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private User dtoFromResultSet(ResultSet resultSet) throws SQLException {
    User user = new User();
    user.setId(resultSet.getLong("id"));
    user.setRole(roleDao.findById(resultSet.getLong("role_id")).orElse(null));
    user.setLogin(resultSet.getString("login"));
    user.setPassword(resultSet.getString("password"));
    user.setEmail(resultSet.getString("email"));
    user.setPhone(resultSet.getString("phone"));
    user.setFirstName(resultSet.getString("first_name"));
    user.setLastName(resultSet.getString("last_name"));
    return user;
  }
}
