package example1_simple_read;

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

public final class Main {

  private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

  public static void main(@NotNull String[] args) {
    FlywayInitializer.initDb();
    try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
      final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

      final Result<Record> result = context
        .select()
        .from(AUTHOR)
        .fetch();

      System.out.println("ID:\t\tFirst name\tLast name\t\t" );
      for (Record record : result) {
        Integer id = record.getValue(AUTHOR.ID);
        String firstName = record.getValue(AUTHOR.FIRST_NAME);
        String lastName = record.getValue(AUTHOR.LAST_NAME);
        System.out.println(id + "\t\t" + firstName + "\t\t" + lastName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
