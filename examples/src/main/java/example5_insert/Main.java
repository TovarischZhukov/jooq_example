package example5_insert;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import generated.tables.Author;
import generated.tables.records.AuthorRecord;
import generated.tables.records.BookRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;

import static generated.Tables.AUTHOR;
import static generated.Tables.BOOK;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      context
        .insertInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
        .values(100, "Herman", "Hesse")
        .values(101, "Alfred", "Doblin")
        .execute();

      context
        .selectFrom(AUTHOR)
        .fetch()
        .forEach(System.out::println);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
