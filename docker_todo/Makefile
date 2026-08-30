.PHONY: build

DOCKER=docker
#DOCKER=podman

up:
	$(DOCKER) compose up -d
down:
	-$(DOCKER) compose down
build:
	$(DOCKER) compose build --no-cache
prune:
	$(DOCKER) container prune -f
	$(DOCKER) image prune -f
prune_volume:
	$(DOCKER) volume prune -f
ps:
	$(DOCKER) compose ps
stats:
	$(DOCKER) stats
sh:
	$(DOCKER) compose exec postgres bash
sql:
	$(DOCKER) compose exec postgres psql -Utodo todo
log:
	$(DOCKER) compose logs --follow
