.PHONY: build

docker:
	@make down
	@make up
	@make ps
up:
	docker compose up -d
down:
	-docker compose down
prune:
	-docker compose down --remove-orphans
	-docker container prune -f
	-docker image prune -fa
ps:
	docker compose ps
stats:
	docker stats
sql:
	docker compose exec postgres psql -Utodo todo
help:
	echo usage make up,down,prune,ps,stats,sql,log
log:
	docker compose logs --follow
