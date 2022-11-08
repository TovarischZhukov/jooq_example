package example8_create_table;

import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.foreignKey;
import static org.jooq.impl.DSL.name;

public final class Main {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void main(@NotNull String[] args) {
        FlywayInitializer.initDb();
        try (Connection conn = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .createTable("price")
                    .column("id", SQLDataType.INTEGER)
                    .column("value", SQLDataType.FLOAT)
                    .column("book_id", SQLDataType.INTEGER)
                    .constraints(foreignKey("book_id").references("book", "id"))
                    .execute();

            context
                    .insertInto(new TableImpl<>(name("price")))
                    .values(1, 10.5, 2)
                    .execute();

            context
                    .selectFrom("price")
                    .fetch()
                    .forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
