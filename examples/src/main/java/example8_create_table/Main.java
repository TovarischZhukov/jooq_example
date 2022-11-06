package example8_create_table;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Connection;
import java.sql.DriverManager;

import static generated.Tables.AUTHOR;
import static org.jooq.util.postgres.PostgresDataType.INTEGER;
import static org.jooq.util.postgres.PostgresDataType.NAME;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      context
        .createTable("person")
        .column("id", SQLDataType.INTEGER)
        .column("name", SQLDataType.VARCHAR)
        .execute();

      context
        .insertInto(new TableImpl<>("person"))
        .values(1, "test_person")
        .execute();

      context
        .selectFrom("person")
        .fetch()
        .forEach(System.out::println);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
