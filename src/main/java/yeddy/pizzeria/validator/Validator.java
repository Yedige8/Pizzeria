package yeddy.pizzeria.validator;

import java.util.Map;

public interface Validator<T> {

  Map<String, String> validate(T object, boolean unique);
}
