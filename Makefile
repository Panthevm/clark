repl:
	  lein repl

ui-build:
	cd ui && clj -m build

jar:
		lein uberjar && java -jar target/app.jar host 0.0.0.0 port 8080

ui-repl:
		cd ui && DEBUG=true clj -A:dev:nrepl -e "(-main)"
