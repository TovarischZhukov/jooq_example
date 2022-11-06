package example6_update;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static generated.Tables.AUTHOR;
import static generated.Tables.BOOK;
import static org.jooq.impl.DSL.select;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      context
        .update(BOOK)
        .set(
          BOOK.PUBLISHED_IN,
          select(DSL.max(BOOK.PUBLISHED_IN)).from(BOOK)
        )
        .where(BOOK.AUTHOR_ID.eq(1))
        .execute();

      context
        .selectFrom(BOOK)
        .fetchStream()
        .forEach(System.out::println);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
