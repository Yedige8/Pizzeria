package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.CategoryDao;
import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;
import yeddy.pizzeria.model.dto.Category;
import yeddy.pizzeria.model.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {

  private final ConnectionPool connectionPool;

  private final CategoryDao categoryDao;

  public ProductDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
    DaoFactory daoFactory = DaoFactory.getInstance();
    categoryDao = daoFactory.getDao(CategoryDao.class);
  }

  @Override
  public Optional<Product> findById(long id) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select p.* from products p where p.id = ? limit 1"
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
  public boolean existsByName(String name) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select count(p.*) from products p where p.name = ?"
      );
      statement.setString(1, name);
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
  public List<Product> findAll() {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select p.* from products p order by p.id desc"
      );
      ResultSet resultSet = statement.executeQuery();
      List<Product> products = new ArrayList<>();
      while (resultSet.next()) {
        products.add(dtoFromResultSet(resultSet));
      }
      return products;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Product> findAllByCategoryAndActive(Category category, boolean active) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select p.* from products p where p.category_id = ? and p.active = ? order by p.name"
      );
      statement.setLong(1, category.getId());
      statement.setBoolean(2, active);
      ResultSet resultSet = statement.executeQuery();
      List<Product> products = new ArrayList<>();
      while (resultSet.next()) {
        products.add(dtoFromResultSet(resultSet));
      }
      return products;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void save(Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "insert into products(category_id, active, name, description, cost) values(?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setLong(1, product.getCategory().getId());
      statement.setBoolean(2, product.isActive());
      statement.setString(3, product.getName());
      statement.setString(4, product.getDescription());
      statement.setInt(5, product.getCost());
      statement.executeUpdate();
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        product.setId(generatedKeys.getLong(1));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void update(Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "update products set category_id = ?, active = ?, name = ?, description = ?, cost = ? where id = ?"
      );
      statement.setLong(1, product.getCategory().getId());
      statement.setBoolean(2, product.isActive());
      statement.setString(3, product.getName());
      statement.setString(4, product.getDescription());
      statement.setInt(5, product.getCost());
      statement.setLong(6, product.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public void deleteById(long id) {

  }

  private Product dtoFromResultSet(ResultSet resultSet) throws SQLException {
    Product product = new Product();
    product.setId(resultSet.getLong("id"));
    product.setCategory(categoryDao.findById(resultSet.getLong("category_id")).orElse(null));
    product.setActive(resultSet.getBoolean("active"));
    product.setName(resultSet.getString("name"));
    product.setDescription(resultSet.getString("description"));
    product.setCost(resultSet.getInt("cost"));
    return product;
  }
}
