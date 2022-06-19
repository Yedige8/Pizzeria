package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Product;
import yeddy.pizzeria.model.dto.Review;
import yeddy.pizzeria.model.dto.User;

import java.util.List;

public interface ReviewDao extends Dao {

  List<Review> findAllByProduct(Product product);

  boolean existsByUserAndProduct(User user, Product product);

  void save(Review review);
}
