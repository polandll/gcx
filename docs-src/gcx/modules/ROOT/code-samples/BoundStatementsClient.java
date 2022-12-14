package biz.bisso.cstar;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public class BoundStatementsClient extends StatementsClient {
    private static final String INSERT_SONGS_DATA_PREPARED = 
        "INSERT INTO demo.songs (id, title, album, artist, tags) VALUES (?, ?, ?, ?, ?);";
    private static final String INSERT_PLAYLISTS_DATA_PREPARED = 
        "INSERT INTO demo.playlists (id, song_id, title, album, artist) VALUES (?, ?, ?, ?, ?);";
    
    private PreparedStatement insertSongsDataStatement;
    private PreparedStatement insertPlaylistsDataStatement;
    
 	public BoundStatementsClient() {
 	}
    
	@Override
	public void createSchema() {
		session.execute(
				"DROP TABLE IF EXISTS demo.songs");
		session.execute(
				"DROP TABLE IF EXISTS demo.playlists");
		session.execute(
				"CREATE TABLE demo.songs (" +
					"id uuid PRIMARY KEY," + 
					"title text," + 
					"album text," + 
					"artist text," + 
					"tags set<text>," + 
					"data blob" + 
					");");
		session.execute(
				"CREATE TABLE demo.playlists (" +
					"id uuid," +
					"title text," +
					"album text, " + 
					"artist text," +
					"song_id uuid," +
					"PRIMARY KEY (id, title, album, artist)" +
					");");
		System.out.println("demo schema created.");
	}
	
	@Override
	public void loadData() {
		// songs table
 	    insertSongsDataStatement = session.prepare(INSERT_SONGS_DATA_PREPARED);
		Set<String> tags = new HashSet<String>();
		tags.add("jazz");
		tags.add("2013");
		BoundStatement boundStatement = insertSongsDataStatement.bind(
				UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf50"),
				"La Petite Tonkinoise'",
				"Bye Bye Blackbird'",
				"Joséphine Baker",
				tags);
		session.execute(boundStatement);
		tags = new HashSet<String>();
		tags.add("1996");
		tags.add("birds");
		boundStatement = insertSongsDataStatement.bind(
				UUID.fromString("f6071e72-48ec-4fcb-bf3e-379c8a696488"),
				"Die Mösch",
				"In Gold'", 
				"Willi Ostermann",
				tags);
		session.execute(boundStatement);
		tags = new HashSet<String>();
		tags.add("1970");
		tags.add("soundtrack");
		boundStatement = insertSongsDataStatement.bind(
				UUID.fromString("fbdf82ed-0063-4796-9c7c-a3d4f47b4b25"),
				"Memo From Turner",
				"Performance",
				"Mick Jager",
				tags);
		session.execute(boundStatement);
		// playlists table
 	    insertPlaylistsDataStatement = session.prepare(INSERT_PLAYLISTS_DATA_PREPARED);
		boundStatement = insertPlaylistsDataStatement.bind(
				UUID.fromString("2cc9ccb7-6221-4ccb-8387-f22b6a1b354d"),
				UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf50"),
				"La Petite Tonkinoise",
				"Bye Bye Blackbird",
				"Joséphine Baker");
		session.execute(boundStatement);
		boundStatement = insertPlaylistsDataStatement.bind(
				UUID.fromString("2cc9ccb7-6221-4ccb-8387-f22b6a1b354d"),
				UUID.fromString("f6071e72-48ec-4fcb-bf3e-379c8a696488"),
				"Die Mösch",
				"In Gold",
				"Willi Ostermann");
		session.execute(boundStatement);
		boundStatement = insertPlaylistsDataStatement.bind(
				UUID.fromString("3fd2bedf-a8c8-455a-a462-0cd3a4353c54"),
				UUID.fromString("fbdf82ed-0063-4796-9c7c-a3d4f47b4b25"),
				"Memo From Turner",
				"Performance",
				"Mick Jager");
		session.execute(boundStatement);
	}

	public static void main(String[] args) {
		BoundStatementsClient client = new BoundStatementsClient();
		client.connect();
		client.createSchema();
		client.loadData();
		client.close();
	}

}
