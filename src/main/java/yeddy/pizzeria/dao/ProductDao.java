package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Category;
import yeddy.pizzeria.model.dto.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends Dao {

  Optional<Product> findById(long id);

  boolean existsByName(String name);

  List<Product> findAll();

  List<Product> findAllByCategoryAndActive(Category category, boolean active);

  void save(Product product);

  void update(Product product);

  void deleteById(long id);
}
