package just_init_db;

import commons.FlywayInitializer;
import org.jetbrains.annotations.NotNull;

public final class Main {

  public static void main(@NotNull String[] args) {
    try {
      FlywayInitializer.initDb();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
