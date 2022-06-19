package yeddy.pizzeria.module.connection_pool;

public class SingleConnectionPool {

  private static ConnectionPool instance;

  public static ConnectionPool getInstance() {
    if (instance == null) {
      instance = new ConnectionPoolImpl();
    }
    return instance;
  }
}
