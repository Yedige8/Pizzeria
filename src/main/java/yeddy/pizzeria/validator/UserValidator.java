package yeddy.pizzeria.validator;

import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.factory.DaoFactory;
import yeddy.pizzeria.model.dto.User;

import java.util.HashMap;
import java.util.Map;

public class UserValidator implements Validator<User> {

  private static final String LOGIN_PATTERN = "^[a-zA-Z_]\\w+$";

  private static final String EMAIL_PATTERN = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

  private static final String PHONE_PATTERN = "^\\+7\\d{10}$";

  private final UserDao userDao;

  public UserValidator() {
    DaoFactory daoFactory = DaoFactory.getInstance();
    userDao = daoFactory.getDao(UserDao.class);
  }

  @Override
  public Map<String, String> validate(User user, boolean unique) {
    Map<String, String> errors = new HashMap<>();
    if (user.getLogin().isEmpty()) {
      errors.put("login", "Логин обязателен для заполнения");
    } else if (!user.getLogin().matches(LOGIN_PATTERN)) {
      errors.put("login", "Логин может состоять из латинских букв, цифр (не в начале) и символа нижнего подчеркивания");
    } else if (userDao.existsByLogin(user.getLogin())) {
      errors.put("login", "Логин занят другим пользователем");
    }
    if (user.getPassword().isEmpty()) {
      errors.put("password", "Пароль обязателен для заполнения");
    } else if (user.getPassword().length() < 5) {
      errors.put("password", "Минимальная длина пароля 5 символов");
    }
    if (user.getEmail().isEmpty()) {
      errors.put("email", "Email обязателен для заполнения");
    } else if (!user.getEmail().matches(EMAIL_PATTERN)) {
      errors.put("email", "Некорректный формат email адреса");
    } else if (userDao.existsByEmail(user.getEmail())) {
      errors.put("email", "Email занят другим пользователем");
    }
    if (user.getPhone().isEmpty()) {
      errors.put("phone", "Номер телефона обязателен для заполнения");
    } else if (!user.getPhone().matches(PHONE_PATTERN)) {
      errors.put("phone", "Номер телефона вводится в формате +7__________");
    } else if (userDao.existsByPhone(user.getPhone())) {
      errors.put("phone", "Номер телефона занят другим пользователем");
    }
    if (user.getFirstName().isEmpty()) {
      errors.put("firstName", "Имя обязательно для заполнения");
    }
    if (user.getLastName().isEmpty()) {
      errors.put("lastName", "Фамилия обязательна для заполнения");
    }
    return errors;
  }
}
