package yeddy.pizzeria.module.connection_pool;

import java.sql.Connection;

public interface ConnectionPool {

  Connection getConnection();

  void releaseConnection(Connection connection);

  void closeConnections();
}
