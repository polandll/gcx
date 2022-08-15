import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import java.nio.file.Paths;

public class GettingStartedComplete {

    public static void main(String[] args) {

        try (CqlSession session = CqlSession.builder()
                // make sure you change the path to the secure connect bundle below
                .withCloudSecureConnectBundle(Paths.get("/path/to/secure-connect-database_name.zip"))
                .withAuthCredentials("clientId","clientSecret")
                .withKeyspace("demo")
                .build()) {

            session.execute("CREATE TABLE IF NOT EXISTS demo.users ("
                    + " lastname text PRIMARY KEY,"
                    + " age int,"
                    + " city text,"
                    + " email text,"
                    + " firstname text)");

            setUser(session, "Jones", 35, "Austin", "bob@example.com", "Bob");

            getUser(session, "Jones");

            updateUser(session, 36, "Jones");

            getUser(session, "Jones");

            deleteUser(session, "Jones");

        }
    }

    private static void setUser(CqlSession cqlSession, String lastname, int age, String city, String email, String firstname) {

        //TO DO: execute SimpleStatement that inserts one user into the table
        cqlSession.execute(
                SimpleStatement.builder( "INSERT INTO users (lastname, age, city, email, firstname) "
                        +  "VALUES (?,?,?,?,?)")
                        .addPositionalValues(lastname, age, city, email, firstname)
                        .build());
    }

    private static void getUser(CqlSession session, String lastname) {

        //TO DO: execute SimpleStatement that retrieves one user from the table
        //TO DO: print firstname and age of user
        ResultSet rs = session.execute(
                SimpleStatement.builder("SELECT * FROM users WHERE lastname=:n")
                        .addPositionalValue(lastname)
                        .build());

        Row row = rs.one();
        System.out.format("%s %d\n", row.getString("firstname"), row.getInt("age"));
    }


    private static void updateUser(CqlSession session, int age, String lastname) {

        //TO DO: execute a BoundStatement that updates the age of one user
        PreparedStatement prepared = session.prepare(
                "UPDATE users SET age =?  WHERE lastname =?");

        BoundStatement bound = prepared.bind(age, lastname);

        session.execute(bound);

    }

    private static void deleteUser(CqlSession session, String lastname) {

        //TO DO: execute BoundStatement that deletes one user from the table
        PreparedStatement prepared = session.prepare(
                "DELETE FROM users WHERE lastname=?");
        BoundStatement bound = prepared.bind(lastname);
        session.execute(bound);

    }

}