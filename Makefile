.PHONY: all test web mobile compile clean help

# Docker mobile test targets
.PHONY: docker-build docker-up docker-down docker-test-local docker-status docker-logs docker-clean install-app

# Default target
all: test

run: install docker-build docker-up install-app test

# Run all tests
test:
	mvn test

# Run only web tests (Habr.com)
web:
	mvn test -DsuiteXmlFile=src/test/resources/testng-web.xml

# Run only mobile tests (Wikipedia)
mobile:
	mvn test -DsuiteXmlFile=src/test/resources/testng-mobile.xml

# Compile without running tests
compile:
	mvn clean compile

# Clean build artifacts
clean:
	mvn clean

# Install dependencies
install:
	mvn dependency:resolve

# Run specific test class (usage: make run-class CLASS=HabrTests)
run-class:
	mvn -Dtest=$(CLASS) test

# Run specific test method (usage: make run-method TEST=HabrTests#testSearchArticle)
run-method:
	mvn -Dtest=$(TEST) test

# Build Docker image with Wikipedia APK
docker-build:
	./scripts/run-docker-tests.sh build

# Build and start Docker Android emulator
docker-up:
	./scripts/run-docker-tests.sh up

# Stop Docker containers
docker-down:
	./scripts/run-docker-tests.sh down

# Run tests from host against Docker emulator
docker-test-local:
	./scripts/run-docker-tests.sh test-local

# Check Docker environment status
docker-status:
	./scripts/run-docker-tests.sh status

# View Docker logs
docker-logs:
	./scripts/run-docker-tests.sh logs

# Clean Docker resources
docker-clean:
	./scripts/run-docker-tests.sh clean

# Install Wikipedia app in Docker container
install-app:
	./scripts/install-app.sh

# Show help
help:
	@echo "Available targets:"
	@echo "  make run        - Run all tests"
	@echo "  make web        - Run only web tests (Habr.com)"
	@echo "  make mobile     - Run only mobile tests (Wikipedia)"
	@echo "  make compile    - Compile without tests"
	@echo "  make clean      - Clean build artifacts"
	@echo "  make install    - Download dependencies"
	@echo "  make run-class CLASS=HabrTests        - Run specific test class"
	@echo "  make run-method TEST=HabrTests#test   - Run specific test method"
	@echo ""
	@echo "Docker targets:"
	@echo "  make docker-build      - Build Docker image with Wikipedia APK"
	@echo "  make docker-up         - Build and start Android emulator in Docker"
	@echo "  make docker-down       - Stop Docker containers"
	@echo "  make docker-test-local - Run tests from host against Docker emulator"
	@echo "  make docker-status     - Check Docker environment status"
	@echo "  make docker-logs       - View emulator logs"
	@echo "  make docker-clean      - Remove Docker resources"
	@echo "  make install-app       - Install Wikipedia app in Docker container"
	@echo ""
	@echo "  make help       - Show this help"