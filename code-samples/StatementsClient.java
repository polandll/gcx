package biz.bisso.cstar;

public class StatementsClient extends ConnectionClient {
	public StatementsClient() {
	}
	
	public void createSchema() {
		session.execute(
				"DROP TABLE IF EXISTS demo.songs;");
		session.execute(
				"CREATE TABLE demo.songs (" +
					"id uuid PRIMARY KEY," + 
					"title text," + 
					"album text," + 
					"artist text," + 
					"tags set<text>," + 
					"data blob" + 
					");");
		System.out.println("demo schema created");
	}
	
	public void loadData() {
		session.execute(
		        "INSERT INTO demo.songs (id, title, album, artist, tags) " +
		        "VALUES (" +
		            "756716f7-2e54-4715-9f00-91dcbea6cf50," +
		            "'La Petite Tonkinoise'," +
		            "'Bye Bye Blackbird'," +
		            "'Joséphine Baker'," +
		            "{'jazz', '2013'})" +
		        ";");
		System.out.println("data loaded into demo schema");
	}

	public static void main(String[] args) {
		StatementsClient client = new StatementsClient();
		client.connect();
		client.createSchema();
		client.loadData();
		client.close();
	}
}
