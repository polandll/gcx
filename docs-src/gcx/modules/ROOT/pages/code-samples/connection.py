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
        self.session = None
    
    def connect(self):
        cloud_config = {
            "secure_connect_bundle" : self.SECURE_CONNECT_BUNDLE
        }
        authProvider = PlainTextAuthProvider(self.CLIENT_ID, self.CLIENT_SECRET)
        cluster = Cluster(cloud = cloud_config, auth_provider = authProvider)
        self.session = cluster.connect(self.KEYSPACE)
        for host in cluster.metadata.all_hosts():
            print(f"{host}")

def main():
    client = ConnectionClient()
    client.connect()

if __name__ == "__main__":
    main()
