package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.RoleDao;
import yeddy.pizzeria.model.dto.Role;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RoleDaoImpl implements RoleDao {

  private final ConnectionPool connectionPool;

  public RoleDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
  }

  @Override
  public Optional<Role> findById(long roleId) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select r.* from roles r where r.id = ? limit 1"
      );
      statement.setLong(1, roleId);
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
  public Optional<Role> findByServiceName(String serviceName) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select r.* from roles r where r.service_name = ? limit 1"
      );
      statement.setString(1, serviceName);
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

  private Role dtoFromResultSet(ResultSet resultSet) throws SQLException {
    Role role = new Role();
    role.setId(resultSet.getLong("id"));
    role.setServiceName(resultSet.getString("service_name"));
    role.setDisplayName(resultSet.getString("display_name"));
    return role;
  }
}










