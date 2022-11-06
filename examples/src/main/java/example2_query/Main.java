package example2_query;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
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

      final var records = context
        .select(
          AUTHOR.FIRST_NAME,
          AUTHOR.LAST_NAME,
          count().as("book_count")
        )
        .from(AUTHOR)
        .join(BOOK).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
        .where(BOOK.LANGUAGE.eq("DE")).and(BOOK.PUBLISHED_IN.gt(1940))
        .groupBy(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
        .having(count().gt(1))
        .orderBy(AUTHOR.LAST_NAME.asc().nullsFirst())
        .limit(2)
        .offset(1)
//        .forUpdate()
        .fetch();
      System.out.println(records.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
