import pulsar

service_url = '<service-url>'

# Use default CA certs for your environment
# RHEL/CentOS:
trust_certs='/etc/ssl/certs/ca-bundle.crt'
# Debian/Ubuntu:
# trust_certs='/etc/ssl/certs/ca-certificates.crt'
# OSX:
# Export the default certificates to a file, then use that file:
#    security find-certificate -a -p /System/Library/Keychains/SystemCACertif
# trust_certs='./ca-certificates.crt'

token='<streaming-token>'

client = pulsar.Client(service_url,
                      authentication=pulsar.AuthenticationToken(token),
                      tls_trust_certs_file_path=trust_certs)

producer = client.create_producer('<topic-url>')

for i in range(10):
  producer.send(('Hello World! %d' % i).encode('utf-8'))

client.close()