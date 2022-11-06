package example9_activeRecord;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import generated.tables.Book;
import generated.tables.records.BookRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Connection;
import java.sql.DriverManager;

import static generated.tables.Book.BOOK;

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
      context.deleteFrom(BOOK).execute();

      final BookRecord bookRecord = context.newRecord(BOOK);
      bookRecord
        .setTitle("New Book")
        .setLanguage("RU")
        .setPublishedIn(2020);
      bookRecord.store();
      Integer id = bookRecord.getId();
      System.out.println(bookRecord);

      bookRecord.setPublishedIn(2021);
      bookRecord.store();

      System.out.println("====UPDATED 1====");
      final BookRecord sameBook = context.fetchOne(BOOK, BOOK.ID.eq(id));
      System.out.println(sameBook);
      sameBook.setTitle("Another New Book");
      sameBook.store();
      System.out.println("====UPDATED 2====");

      context.fetch(BOOK).forEach(System.out::println);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
