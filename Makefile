postgres-up:
		cd server && postgres -D pgdata
repl:
		cd server && lein repl
ui-repl:
		cd ui && DEBUG=true clj -A:dev:nrepl -e "(-main)"
ring:
		cd server && lein ring server
