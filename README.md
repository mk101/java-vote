## Билд

Для сборки используется maven

```bash
mvn clean package
```

## Запуск

* В режиме сервера 
```bash
java -jar .\launcher\target\launcher-1.0-SNAPSHOT-jar-with-dependencies.jar server
```

* В режиме клиента
```bash
java -jar .\launcher\target\launcher-1.0-SNAPSHOT-jar-with-dependencies.jar client
```

## Логирование

Логи сервера хранятся в файле `server.log`
