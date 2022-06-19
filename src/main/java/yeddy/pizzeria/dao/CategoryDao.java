package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao extends Dao {

  Optional<Category> findById(long id);

  List<Category> findAll();
}
