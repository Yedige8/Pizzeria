package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.StatusDao;
import yeddy.pizzeria.model.dto.Status;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatusDaoImpl implements StatusDao {

  private final ConnectionPool connectionPool;

  public StatusDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
  }

  @Override
  public Optional<Status> findById(long statusId) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select s.* from statuses s where s.id = ? limit 1"
      );
      statement.setLong(1, statusId);
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
  public Optional<Status> findByServiceName(String serviceName) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select s.* from statuses s where s.service_name = ? limit 1"
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

  @Override
  public List<Status> findAll() {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select s.* from statuses s"
      );
      ResultSet resultSet = statement.executeQuery();
      List<Status> statuses = new ArrayList<>();
      while (resultSet.next()) {
        statuses.add(dtoFromResultSet(resultSet));
      }
      return statuses;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private Status dtoFromResultSet(ResultSet resultSet) throws SQLException {
    Status status = new Status();
    status.setId(resultSet.getLong("id"));
    status.setServiceName(resultSet.getString("service_name"));
    status.setDisplayName(resultSet.getString("display_name"));
    return status;
  }
}
