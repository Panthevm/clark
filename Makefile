postgres-up:
		postgres -D pgdata

repl:
		lein repl

ui-repl:
		cd ui && DEBUG=true clj -A:dev:nrepl -e "(-main)"
