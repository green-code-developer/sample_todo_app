.PHONY: build

docker:
	@make purge
	docker compose build
	@make up
	@make ps
purge:
	-docker compose down --remove-orphans
up:
	docker compose up -d
down:
	docker compose down
ps:
	docker compose ps
stats:
	docker stats
sql:
	docker compose exec postgres psql -Utodo todo
log:
	docker compose logs --follow
