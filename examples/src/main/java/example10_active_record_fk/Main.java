package example10_active_record_fk;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import generated.Keys;
import generated.tables.records.BookRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static generated.tables.Book.BOOK;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      final int value = 1948;
      final var records = context.fetch(BOOK, BOOK.PUBLISHED_IN.eq(value));
      for (BookRecord record : records) {
        if ("Orwell".equals(record.fetchParent(Keys.BOOK__BOOK_AUTHOR_ID_FKEY).getLastName())) {
          record.delete();
        }
      }
      context.fetch(BOOK).forEach(System.out::println);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
