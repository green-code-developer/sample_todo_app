.PHONY: build

DOCKER_DIR=docker_todo

docker:
	cd $(DOCKER_DIR) && make
run:
	./gradlew bootrun
jar:
	./gradlew bootJar
	ls -l  build/libs/*-SNAPSHOT.jar

