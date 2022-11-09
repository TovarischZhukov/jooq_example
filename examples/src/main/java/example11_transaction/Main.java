package example11_transaction;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
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

            context.transaction(transaction -> {
                transaction.dsl()
                        .insertInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                        .values(100, "Herman", "Hesse")
                        .execute();

                transaction.dsl()
                        .insertInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                        .values(101, "Alfred", "Doblin")
                        .execute();
            });

            context
                    .selectFrom(AUTHOR)
                    .fetch()
                    .forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}