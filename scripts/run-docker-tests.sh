#!/bin/bash

# Main script to run mobile tests in Docker environment
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Docker Mobile Test Runner ===${NC}"

# Check for KVM support
check_kvm() {
    if [ ! -e /dev/kvm ]; then
        echo -e "${RED}ERROR: KVM is not available on this system${NC}"
        echo "Please ensure:"
        echo "  1. Your CPU supports hardware virtualization (VT-x/AMD-V)"
        echo "  2. Virtualization is enabled in BIOS"
        echo "  3. KVM module is loaded: sudo modprobe kvm"
        exit 1
    fi

    if [ ! -r /dev/kvm ] || [ ! -w /dev/kvm ]; then
        echo -e "${YELLOW}WARNING: Current user may not have KVM access${NC}"
        echo "Run: sudo usermod -aG kvm \$USER && newgrp kvm"
    fi

    echo -e "${GREEN}KVM is available${NC}"
}

# Build Docker image
build_image() {
    echo -e "${GREEN}Building Docker image (Wikipedia APK will be downloaded)...${NC}"
    docker-compose build
    echo -e "${GREEN}Docker image built successfully${NC}"
}

# Parse command line arguments
ACTION="${1:-up}"

case $ACTION in
    build)
        echo -e "${GREEN}Building Docker image...${NC}"
        check_kvm
        build_image
        ;;

    up|start)
        echo -e "${GREEN}Starting Docker environment...${NC}"
        check_kvm
        docker-compose up -d --build android-emulator
        echo ""
        echo -e "${GREEN}Android emulator is starting...${NC}"
        echo "Access noVNC at: http://localhost:6080"
        echo "Appium server at: http://localhost:4723"
        echo ""
        echo "Wait for the emulator to fully boot (2-5 minutes)"
        echo "Check status with: $0 status"
        ;;

    down|stop)
        echo -e "${YELLOW}Stopping Docker environment...${NC}"
        docker-compose down
        ;;

    test-local)
        echo -e "${GREEN}Running tests from host against Docker emulator...${NC}"
        ./scripts/wait-for-emulator.sh
        mvn test -DsuiteXmlFile=src/test/resources/testng-mobile.xml
        ;;

    status)
        echo -e "${GREEN}Checking environment status...${NC}"
        docker-compose ps
        echo ""
        echo "Appium status:"
        curl -s http://localhost:4723/status 2>/dev/null | head -c 500 || echo "Not ready"
        ;;

    logs)
        docker-compose logs -f android-emulator
        ;;

    clean)
        echo -e "${YELLOW}Cleaning up Docker resources...${NC}"
        docker-compose down -v --rmi local
        ;;

    *)
        echo "Usage: $0 {build|up|down|test-local|status|logs|clean}"
        echo ""
        echo "Commands:"
        echo "  build      - Build Docker image with Wikipedia APK"
        echo "  up         - Build (if needed) and start Android emulator container"
        echo "  down       - Stop all containers"
        echo "  test-local - Run tests from host against Docker emulator"
        echo "  status     - Check container and Appium status"
        echo "  logs       - Follow container logs"
        echo "  clean      - Remove containers, volumes, and images"
        exit 1
        ;;
esac
