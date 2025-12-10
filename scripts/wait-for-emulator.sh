#!/bin/bash

# Script to wait for Android emulator and Appium to be ready
set -e

APPIUM_URL="${APPIUM_SERVER_URL:-http://localhost:4723}"
MAX_RETRIES=60
RETRY_INTERVAL=10

echo "Waiting for Appium server at $APPIUM_URL..."

for i in $(seq 1 $MAX_RETRIES); do
    if curl -sf "$APPIUM_URL/status" > /dev/null 2>&1; then
        echo "Appium server is ready!"

        # Additional check for emulator readiness
        RESPONSE=$(curl -s "$APPIUM_URL/status")
        if echo "$RESPONSE" | grep -q '"ready":true'; then
            echo "Appium is fully ready. Starting tests..."
            exit 0
        fi
    fi

    echo "Attempt $i/$MAX_RETRIES: Appium not ready yet. Waiting $RETRY_INTERVAL seconds..."
    sleep $RETRY_INTERVAL
done

echo "ERROR: Appium server failed to become ready after $MAX_RETRIES attempts"
exit 1
