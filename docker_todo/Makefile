.PHONY: build

docker:
	@make down
	@make up
	@make ps
purge:
	-docker compose down --remove-orphans
	-docker container prune -f
	-docker image prune -fa
up:
	docker compose up -d
down:
	-docker compose down
ps:
	docker compose ps
stats:
	docker stats
sql:
	docker compose exec postgres psql -Utodo todo
log:
	docker compose logs --follow
