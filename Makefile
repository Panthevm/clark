repl:
	  lein repl

jar:
		lein uberjar && java -jar target/app.jar host 0.0.0.0 port 8080

ui-repl:
		cd ui && DEBUG=true clj -A:dev:nrepl -e "(-main)"
