.PHONY: build run test clean package docker-build docker-run docker-push

APP_NAME = nexuspay
VERSION ?= 1.0.0

DOCKER_USERNAME ?= nsahil992
IMAGE = $(DOCKER_USERNAME)/$(APP_NAME):$(VERSION)

# ----- MAKE TARGETS -----

build:
	mvn clean install

run:
	mvn spring-boot:run

package:
	mvn clean package -DskipTests

test:
	mvn test

lint:
	mvn checkstyle:check || true

clean:
	mvn clean

# ----- DOCKER TARGETS -----
docker-test:
	hadolint Dockerfile

docker-build:
	docker build -t $(IMAGE) .

docker-run:
	docker run --env-file .env -p 8081:8081 $(IMAGE)

docker-push:
	docker push $(IMAGE)

# ----- DOCKER COMPOSE TARGETS -----

compose-up:
	docker compose up

compose-down:
	docker compose down

