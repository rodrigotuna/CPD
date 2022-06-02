#! /usr/bin/bash

cd out/production/untitled
rmiregistry &
java Store 224.0.1.0 8000 127.0.0.1 8000 &
java Store 224.0.1.0 8000 127.0.0.2 8000 &
java Store 224.0.1.0 8000 127.0.0.3 8000 &
java Store 224.0.1.0 8000 127.0.0.4 8000 &
java Store 224.0.1.0 8000 127.0.0.5 8000 &

java TestClient 127.0.0.1:8000 join &
java TestClient 127.0.0.2:8000 join &
java TestClient 127.0.0.3:8000 join &
java TestClient 127.0.0.4:8000 join &
java TestClient 127.0.0.5:8000 join &

java TestClient 127.0.0.3:8000 put test
## retorna hash

java TestClient 127.0.0.3:8000 get {hash}
##retorna ficheiro


java TestClient 127.0.0.3:8000 delete {hash}
##acho que nao retorna nada

### Membership
### key/value -> so com o cliente receber e mandar ficheiros
## mandar ficheiros entre nós