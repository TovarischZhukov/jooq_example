package example3_select;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static generated.Tables.AUTHOR;
import static generated.Tables.BOOK;
import static org.jooq.impl.DSL.count;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      final Result<Record3<String, String, String>> records = context
        .select(
          BOOK.TITLE,
          AUTHOR.FIRST_NAME,
          AUTHOR.LAST_NAME
        )
        .from(BOOK).join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
        .where(BOOK.PUBLISHED_IN.eq(1948))
        .fetch();

      records.forEach(record -> {
        System.out.println(
          record.get(0, String.class)
            + " " + record.get(1)
            + " " + record.get(AUTHOR.LAST_NAME)
        );
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
