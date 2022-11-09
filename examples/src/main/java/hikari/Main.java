package hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import commons.FlywayInitializer;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.util.Properties;

import static generated.tables.Author.AUTHOR;

public final class Main {
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void main(@NotNull String[] args) {
        FlywayInitializer.initDb();
        var props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.user", CREDS.login());
        props.setProperty("dataSource.password", CREDS.password());
        props.setProperty("dataSource.databaseName", CREDS.dbName());
        var config = new HikariConfig(props);

        try (var ds = new HikariDataSource(config)) {
            try (var conn = ds.getConnection()) {
                final var context = DSL.using(conn, SQLDialect.POSTGRES);

                final var result = context
                        .select()
                        .from(AUTHOR)
                        .fetch();

                System.out.println("ID:\t\tFirst name\tLast name\t\t");
                for (var record : result) {
                    Integer id = record.getValue(AUTHOR.ID);
                    String firstName = record.getValue(AUTHOR.FIRST_NAME);
                    String lastName = record.getValue(AUTHOR.LAST_NAME);
                    System.out.println(id + "\t\t" + firstName + "\t\t" + lastName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
