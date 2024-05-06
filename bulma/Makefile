.PHONY: build

build:
	npm run build-bulma
	ls -l app-bulma.css app-bulma.css.map
	@make cp

cp:
	./node_modules/.bin/cleancss -o ../src/main/resources/static/assets/css/app-bulma.css app-bulma.css

