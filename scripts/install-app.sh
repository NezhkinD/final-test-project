#!/bin/bash

# Install Wikipedia APK in Docker container
set -e

CONTAINER_NAME="android-emulator"
APK_PATH="/tmp/apk/wikipedia.apk"
APP_PACKAGE="org.wikipedia"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

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

# Check if emulator is booted
is_emulator_ready() {
    local boot_completed
    boot_completed=$(docker exec "$CONTAINER_NAME" adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r' || echo "")
    [ "$boot_completed" = "1" ]
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
        if is_emulator_ready; then
            log_info "Emulator is ready"
            return 0
        fi

        attempt=$((attempt + 1))
        echo -ne "\r  Waiting... (${attempt}/${max_attempts})"
        sleep 5
    done

    echo ""
    log_error "Emulator failed to boot in time"
    return 1
}

# Main
main() {
    log_info "=== Wikipedia APK Installer ==="

    # Check container
    if ! is_container_running; then
        log_error "Container '$CONTAINER_NAME' is not running"
        log_info "Start it with: make docker-up"
        exit 1
    fi

    log_info "Container '$CONTAINER_NAME' is running"

    # Wait for emulator
    wait_for_emulator

    # Check if already installed
    if is_app_installed; then
        log_info "Wikipedia app is already installed"
        exit 0
    fi

    # Install APK
    log_info "Installing Wikipedia APK..."
    if docker exec "$CONTAINER_NAME" adb install "$APK_PATH"; then
        log_info "Wikipedia app installed successfully"
    else
        log_error "Failed to install APK"
        exit 1
    fi

    # Verify installation
    if is_app_installed; then
        log_info "Installation verified"
    else
        log_error "Installation verification failed"
        exit 1
    fi

    log_info "=== Done ==="
}

main "$@"
