package yeddy.pizzeria;

import java.util.Locale;

public class Global {

  public static final Locale[] SUPPORTED_LOCALES = new Locale[]{
      new Locale("ru", "RU"),
      new Locale("en", "US")
  };

  public static final Locale DEFAULT_LOCALE = new Locale("ru", "RU");

  public static final String DB_DRIVER_CLASS = "org.postgresql.Driver";

  public static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/pizzeria";

  public static final String DB_USERNAME = "postgres";

  public static final String DB_PASSWORD = "123";
}
