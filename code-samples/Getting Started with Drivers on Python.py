from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider

def set_user(session, lastname, age, city, email, firstname):
     # TO DO: execute SimpleStatement that inserts one user into the table
    session.execute("INSERT INTO users (lastname, age, city, email, firstname) VALUES (%s,%s,%s,%s,%s)", [lastname, age, city, email, firstname])

def get_user(session, lastname):
    # TO DO: execute SimpleStatement that retrieves one user from the table
    # TO DO: print firstname and age of user
    result = session.execute("SELECT * FROM users WHERE lastname = %(surname)s", {'surname':lastname}).one()
    print result.firstname, result.age

def update_user(session, new_age, lastname):
    # TO DO: execute a BoundStatement that updates the age of one user
    prepared = session.prepare("UPDATE users SET age = ? WHERE lastname = ?")
    session.execute(prepared, [new_age, lastname])

def delete_user(session, lastname):
    # TO DO: execute a BoundStatement that updates the age of one user
    prepared = session.prepare("DELETE FROM users WHERE lastname = ?")
    session.execute(prepared, [lastname])

def main():

    cloud_config= {
        'secure_connect_bundle': '/path/to/secure-connect-database_name.zip'
    }
    auth_provider = PlainTextAuthProvider('clientId', 'clientSecret')
    cluster = Cluster(cloud=cloud_config, auth_provider=auth_provider)
    session = cluster.connect('demo')

    session.execute(
        """
        CREATE TABLE IF NOT EXISTS demo.users (
            lastname text PRIMARY KEY,
            age int,
            city text,
            email text,
            firstname text);
        """
        )

    lastname = "Jones"
    age = 35
    city = "Austin"
    email = "bob@example.com"
    firstname = "Bob"
    new_age = 36

    set_user(session, lastname, age, city, email, firstname)

    get_user(session, lastname)

    update_user(session, new_age, lastname)

    get_user(session, lastname)

    delete_user(session, lastname)

if __name__ == "__main__":
    main()