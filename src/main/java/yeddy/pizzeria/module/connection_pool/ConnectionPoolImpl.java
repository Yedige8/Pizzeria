package yeddy.pizzeria.module.connection_pool;

import yeddy.pizzeria.Global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolImpl implements ConnectionPool {

  private static final int INITIAL_POOL_SIZE = 10;

  private final List<Connection> connectionPool;

  private final List<Connection> usedConnections;

  ConnectionPoolImpl() {
    List<Connection> connectionPoll = new ArrayList<>(INITIAL_POOL_SIZE);
    for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
      connectionPoll.add(createConnection());
    }
    this.connectionPool = connectionPoll;
    this.usedConnections = new ArrayList<>();
  }

  private static Connection createConnection() {
    try {
      Class.forName(Global.DB_DRIVER_CLASS);
      return DriverManager.getConnection(Global.DB_CONNECTION_URL, Global.DB_USERNAME, Global.DB_PASSWORD);
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Connection getConnection() {
    Connection connection = connectionPool.remove(connectionPool.size() - 1);
    usedConnections.add(connection);
    return connection;
  }

  @Override
  public void releaseConnection(Connection connection) {
    connectionPool.add(connection);
    usedConnections.remove(connection);
  }

  @Override
  public void closeConnections() {
    try {
      usedConnections.forEach(this::releaseConnection);
      for (Connection connection : connectionPool) {
        connection.close();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
