.DEFAULT_GOAL := build

GRADLE := ./gradlew
ifeq ($(OS),Windows_NT)
GRADLE := "$(CURDIR)/gradlew.bat"
endif
GRADLE_ARGS ?=

.PHONY: help build check test clean client server game-test data

help:
	@echo make or make build - Build the mod JAR
	@echo make check - Run Gradle checks and headless game tests
	@echo make test - Run JVM tests
	@echo make client - Launch the development client
	@echo make server - Launch the dedicated development server
	@echo make game-test - Run the headless Forge game test server
	@echo make data - Generate mod resources
	@echo make clean - Remove Gradle build outputs

build test clean:
	$(GRADLE) $(GRADLE_ARGS) $@

check:
	$(GRADLE) $(GRADLE_ARGS) check runGameTestServer

client:
	$(GRADLE) $(GRADLE_ARGS) runClient

server:
	$(GRADLE) $(GRADLE_ARGS) runServer

game-test:
	$(GRADLE) $(GRADLE_ARGS) runGameTestServer

data:
	$(GRADLE) $(GRADLE_ARGS) runData
