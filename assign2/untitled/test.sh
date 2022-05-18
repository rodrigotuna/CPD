#! /usr/bin/bash

cd out/production/untitled
rmiregistry &
java Store 224.0.1.0 8000 127.0.0.1 8000 &
java Store 224.0.1.0 8000 127.0.0.2 8000 &
java TestClient 127.0.0.1:8000 join &
java TestClient 127.0.0.2:8000 join &