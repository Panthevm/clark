postgres-up:
		cd server && postgres -D pgdata

repl:
		cd server && lein repl 
ring:
		cd server && lein ring server
