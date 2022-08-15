#!/usr/bin/env python
# -*- coding: utf-8 -*-

from cassandra.auth import PlainTextAuthProvider
from cassandra.cluster import Cluster

class ConnectionClient:
    SECURE_CONNECT_BUNDLE = "/path/to/secure-connect-driver-demo.zip"
    CLIENT_ID = "client-id"
    CLIENT_SECRET = "client-secret"
    KEYSPACE = "demo"

    def __init__(self):
        self.cluster = None
        self.session = None
    
    def connect(self):
        cloud_config = {
            "secure_connect_bundle" : self.SECURE_CONNECT_BUNDLE
        }
        authProvider = PlainTextAuthProvider(self.CLIENT_ID, self.CLIENT_SECRET)
        self.cluster = Cluster(cloud = cloud_config, auth_provider = authProvider)
        self.session = self.cluster.connect(self.KEYSPACE)
        for host in self.cluster.metadata.all_hosts():
            print(f"{host}")
            
    def close(self):
        self.cluster.shutdown()

class StatementsClient(ConnectionClient):
    def __init__(self):
        super().__init__()
        
    def createSchema(self):
        self.session.execute("DROP TABLE IF EXISTS demo.songs;", timeout = 10.0)
        self.session.execute("""
            CREATE TABLE demo.songs (
                id uuid PRIMARY KEY,
                title text,
                album text,
                artist text,
                tags set<text>,
                data blob
            );
        """)
        print('schema created in demo keyspace')

    def loadData(self):
        self.session.execute("""
            INSERT INTO demo.songs (id, title, album, artist, tags)
            VALUES (
                756716f7-2e54-4715-9f00-91dcbea6cf50,
                'La Petite Tonkinoise',
                'Bye Bye Blackbird',
                'Joséphine Baker',
                {'jazz', '2013'}
            );
        """)
        print('data loaded into demo schema')
        

def main():
    client = StatementsClient()
    client.connect()
    client.createSchema()
    client.loadData()
    client.close()

if __name__ == "__main__":
    main()
