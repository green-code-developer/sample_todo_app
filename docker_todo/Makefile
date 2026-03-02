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
	-docker container prune -f
	-docker image prune -fa
	-docker volume prune -f
ps:
	docker compose ps
stats:
	docker stats
sh:
	docker compose exec postgres bash
sql:
	docker compose exec postgres psql -Utodo todo
help:
	echo usage make up,down,prune,ps,stats,sql,log
log:
	docker compose logs --follow
