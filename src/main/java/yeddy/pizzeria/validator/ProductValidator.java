package yeddy.pizzeria.validator;

import yeddy.pizzeria.dao.ProductDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductValidator implements Validator<Product> {

  private final ProductDao productDao;

  public ProductValidator() {
    DaoFactory daoFactory = DaoFactory.getInstance();
    productDao = daoFactory.getDao(ProductDao.class);
  }

  @Override
  public Map<String, String> validate(Product product, boolean unique) {
    Map<String, String> errors = new HashMap<>();
    if (product.getCategory() == null) {
      errors.put("category", "Категория товара обязательна для выбора");
    }
    if (product.getName().isEmpty()) {
      errors.put("name", "Название обязательно для заполнения");
    } else if (unique && productDao.existsByName(product.getName())) {
      errors.put("name", "Товар с таким названием уже существует");
    }
    if (product.getDescription().isEmpty()) {
      errors.put("description", "Описание не может быть пустым");
    }
    if (product.getCost() <= 0) {
      errors.put("cost", "Стоимость не может быть равна нулю или быть отрицательной");
    }
    return errors;
  }
}
