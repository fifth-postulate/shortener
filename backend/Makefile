.PHONY: all, image

JAR=build/libs/shortener-latest.jar

all: ${JAR} image
	@echo "finished building"

image: Dockerfile
	@docker build -t dvberkel/shortener_backend .

${JAR}: src/**
	@./gradlew build