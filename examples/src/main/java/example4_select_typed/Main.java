package example4_select_typed;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import generated.tables.records.AuthorRecord;
import generated.tables.records.BookRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static generated.Tables.AUTHOR;
import static generated.Tables.BOOK;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      final Record record = context
        .select()
        .from(BOOK).join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
        .where(BOOK.ID.eq(1))
        .fetchOne();

      final BookRecord bookRecord = record.into(BOOK);
      final AuthorRecord authorRecord = record.into(AUTHOR);

      System.out.println(bookRecord);
      System.out.println(authorRecord);

      final BookRecord anotherRecord = context
        .selectFrom(BOOK)
        .where(BOOK.ID.eq(1))
        .fetchOne();
      System.out.println(anotherRecord);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
