const cassandra = require('cassandra-driver');

const client = new cassandra.Client({
  cloud: { secureConnectBundle: 'path/to/secure-connect-DATABASE_NAME.zip' },
  credentials: { username: 'clientId', password: 'clientSecret' }
  keyspace: 'demo'
});

function createTable(){
    const query = "CREATE TABLE IF NOT EXISTS demo.users (lastname text PRIMARY KEY, age int, city text, email text, firstname text);";
    return client.execute(query);
}

function insertUser(lastname, age, city, email, firstname) {
  // TO DO: execute a simple statement that inserts one user into the table
  const insert = 'INSERT INTO users (lastname, age, city, email, firstname) VALUES (?,?,?,?,?)';
  const params = [ lastname, age, city, email, firstname ];
  return client.execute(insert, params, { prepare : true });
}

function selectUser(lastname) {
  // TO DO: execute a prepared statement that retrieves one user from the table
  const select = 'SELECT firstname, age FROM users WHERE lastname = :lastname';
  const params = [ lastname ] ;
  return client.execute(select, params, { prepare : true });
}

function updateUser(age, lastname) {
  // TO DO: execute a prepared statement that updates the age of one user
  const update = 'UPDATE users SET age = ? WHERE lastname = ?';
  return client.execute(update, [ age, lastname ], { prepare : true } )
}

function deleteUser(lastname) {
  // TO DO: execute a prepared that deletes one user from the table
  const remove = 'DELETE FROM users WHERE lastname = ?';
  const params = [ lastname ];
  return client.execute(remove, params, { prepare: true })
}

async function example() {
  await client.connect();
  await createTable();
  await insertUser('Jones', 35, 'Austin', 'bob@example.com', 'Bob');
  const rs1 = await selectUser('Jones');
  const user1 = rs1.first();
  if (user1) {
    console.log("name = %s, age = %d", user1.firstname, user1.age);
  } else {
    console.log("No results");
  }
  await updateUser(36, 'Jones');
  const rs2 = await selectUser('Jones');
  const user2 = rs2.first();
  if (user2) {
    console.log("name = %s, age = %d", user2.firstname, user2.age);
  } else {
    console.log("No results");
  }
  await deleteUser('Jones');

  await client.shutdown();
}

example();