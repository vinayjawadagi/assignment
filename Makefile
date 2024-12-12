git:
	git add .
	@echo "Committing changes with message: $(m)"
	git commit -m "$(m)"
	git push origin main
