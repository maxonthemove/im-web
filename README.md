# Mini-IM-Web

A lightweight web chat room, based on minimal hardware resource dependency, aims to reduce server resource consumption as much as possible.
## Install

```bash
git clone https://github.com/maxonthemove/im-web.git
cd im-web
# Compiled successfully in JDK1.8 environment. If compilation fails, please switch the JDK version.
mvn clean package -DskipTests=true
java -jar target/IM-0.0.1-SNAPSHOT.jar
```

Then visit `http://localhost:8888` in your browser.

## Features
- [x] text support
- [ ] picture support
- [ ] data persistence
- [ ] encryption
