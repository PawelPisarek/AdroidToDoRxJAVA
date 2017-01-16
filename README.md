# AdroidToDoRxJAVA

1. (Opcjonalny) npm install -g json-server 
2. cd server/
3. json-server --watch db.json
 - plik db.json:
 {
  "task": [
    {
      "id": 1,
      "title": "json-server"
    },
	{
      "id": 2,
      "title": "json-server"
    },
	{
      "id": 3,
      "title": "json-server"
    }
  ]
 }
4. node server.js
5. node long.js

- /POST http://localhost:2000/task z body: {"title":"task"} <- dodaje task do bazy danych
- /GET  http://localhost:2000/task-new?email=1@wp.pl <- sprawdza czy użytkownik ma nowe taski
- /GET  http://localhost:2001/task-new?email=1@wp.pl <- sprawdza czy użytkownik ma nowe taski za pomocą long polling <- port 2001 

 Niestety gdy robię zapytanie na port 2001 okazuje się, że port 2000 też jest zablokowany, dlaczego?








