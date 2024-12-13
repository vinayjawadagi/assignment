git:
	git add .
	@echo "Committing changes with message: $(m)"
	git commit -m "$(m)"
	git push origin main

run:
	javac -d out src/msg/*.java
	jar cvfe MessageAlertSystem.jar msg.MessageAlertSim -C out .
	clear
	java -jar MessageAlertSystem.jar $(wordlist 2,6,$(MAKECMDGOALS))

%:
	@: