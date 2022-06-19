package yeddy.pizzeria.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItem {

  private User user;

  private Product product;

  private Integer amount;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}
