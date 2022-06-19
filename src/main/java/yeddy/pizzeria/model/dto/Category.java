package yeddy.pizzeria.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Category {

  private Long id;

  private String name;

  private List<Product> products;

  public Category(ResultSet resultSet) {
    try {
      id = resultSet.getLong("id");
      name = resultSet.getString("name");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
