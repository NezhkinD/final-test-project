#!/bin/bash

set -e

CONTAINER_NAME="android-emulator"
APPIUM_URL="http://127.0.0.1:4723"
APK_PATH="/tmp/apk/wikipedia.apk"
APP_PACKAGE="org.wikipedia"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if container is running
is_container_running() {
    docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"
}

# Check if Appium is ready
is_appium_ready() {
    local response
    response=$(curl -s "${APPIUM_URL}/status" 2>/dev/null || echo "")
    echo "$response" | grep -q '"ready":true'
}

# Check if app is installed
is_app_installed() {
    docker exec "$CONTAINER_NAME" adb shell pm list packages 2>/dev/null | grep -q "$APP_PACKAGE"
}

# Wait for emulator to boot
wait_for_emulator() {
    log_info "Waiting for emulator to boot..."
    local max_attempts=60
    local attempt=0

    while [ $attempt -lt $max_attempts ]; do
        local boot_completed
        boot_completed=$(docker exec "$CONTAINER_NAME" adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r' || echo "")

        if [ "$boot_completed" = "1" ]; then
            log_info "Emulator booted successfully"
            return 0
        fi

        attempt=$((attempt + 1))
        echo -ne "\r  Waiting for boot... (${attempt}/${max_attempts})"
        sleep 5
    done

    echo ""
    log_error "Emulator failed to boot in time"
    return 1
}

# Wait for Appium server
wait_for_appium() {
    log_info "Waiting for Appium server..."
    local max_attempts=40
    local attempt=0

    while [ $attempt -lt $max_attempts ]; do
        if is_appium_ready; then
            log_info "Appium server is ready"
            return 0
        fi

        attempt=$((attempt + 1))
        echo -ne "\r  Waiting for Appium... (${attempt}/${max_attempts})"
        sleep 5
    done

    echo ""
    log_error "Appium server failed to start in time"
    return 1
}

# Main script
main() {
    log_info "=== Mobile Tests Runner ==="

    # Step 1: Check/Start container
    if is_container_running; then
        log_info "Container '$CONTAINER_NAME' is already running"
    else
        log_info "Starting container..."
        docker-compose up -d --build
        log_info "Container started"
    fi

    # Step 2: Wait for emulator
    wait_for_emulator

    # Step 3: Wait for Appium
    wait_for_appium

    # Step 4: Install APK if not installed
    if is_app_installed; then
        log_info "Wikipedia app is already installed"
    else
        log_info "Installing Wikipedia APK..."
        docker exec "$CONTAINER_NAME" adb install "$APK_PATH"
        log_info "APK installed successfully"
    fi

    # Step 5: Run tests
    log_info "Starting mobile tests..."
    echo ""

    mvn test -DsuiteXmlFile=src/test/resources/testng-mobile.xml

    log_info "=== Tests completed ==="
}

main "$@"
