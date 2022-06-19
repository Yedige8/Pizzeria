package yeddy.pizzeria.dao.factory;

import yeddy.pizzeria.dao.UserDao;
import yeddy.pizzeria.dao.impl.UserDaoImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DaoFactory {

  private static DaoFactory instance;

  private final Map<Class<? extends Dao>, Dao> typeToObject = new HashMap<>();

  public <T extends Dao> T getDao(Class<T> clazz) {
    return clazz.cast(typeToObject.get(clazz));
  }

  public <T extends Dao> void register(Class<T> clazz, Dao object) {
    typeToObject.put(clazz, object);
  }

  public static DaoFactory getInstance() {
    if (instance == null) {
      instance = new DaoFactory();
    }
    return instance;
  }
}
