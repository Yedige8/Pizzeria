package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.CategoryDao;
import yeddy.pizzeria.model.dto.Category;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDaoImpl implements CategoryDao {

  private final ConnectionPool connectionPool;

  public CategoryDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
  }

  @Override
  public Optional<Category> findById(long id) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select * from categories where id = ? limit 1"
      );
      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(new Category(resultSet));
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Category> findAll() {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select c.* from categories c order by c.name"
      );
      ResultSet resultSet = statement.executeQuery();
      List<Category> categories = new ArrayList<>();
      while (resultSet.next()) {
        categories.add(new Category(resultSet));
      }
      return categories;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }
}
