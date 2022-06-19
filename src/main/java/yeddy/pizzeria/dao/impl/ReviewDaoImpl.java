package yeddy.pizzeria.dao.impl;

import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.ReviewDao;
import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.Review;
import yeddy.pizzeria.model.dto.User;
import yeddy.pizzeria.module.connection_pool.ConnectionPool;
import yeddy.pizzeria.module.connection_pool.SingleConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoImpl implements ReviewDao {

  private final ConnectionPool connectionPool;

  private final UserDao userDao;

  private final ProductDao productDao;

  public ReviewDaoImpl() {
    connectionPool = SingleConnectionPool.getInstance();
    DaoFactory daoFactory = DaoFactory.getInstance();
    userDao = daoFactory.getDao(UserDao.class);
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  public List<Review> findAllByProduct(Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select r.* from reviews r where r.product_id = ? order by r.id desc"
      );
      statement.setLong(1, product.getId());
      ResultSet resultSet = statement.executeQuery();
      List<Review> reviews = new ArrayList<>();
      while (resultSet.next()) {
        reviews.add(dtoFromResultSet(resultSet));
      }
      return reviews;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean existsByUserAndProduct(User user, Product product) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "select count(r.*) from reviews r where r.user_id = ? and r.product_id = ?"
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
  public void save(Review review) {
    Connection connection = connectionPool.getConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "insert into reviews(user_id, product_id, rating, comment) values(?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setLong(1, review.getUser().getId());
      statement.setLong(2, review.getProduct().getId());
      statement.setInt(3, review.getRating());
      statement.setString(4, review.getComment());
      statement.executeUpdate();
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        review.setId(generatedKeys.getLong(1));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private Review dtoFromResultSet(ResultSet resultSet) throws SQLException {
    Review review = new Review();
    review.setId(resultSet.getLong("id"));
    review.setUser(userDao.findById(resultSet.getLong("user_id")).orElse(null));
    review.setProduct(productDao.findById(resultSet.getLong("product_id")).orElse(null));
    review.setRating(resultSet.getInt("rating"));
    review.setComment(resultSet.getString("comment"));
    return review;
  }
}
