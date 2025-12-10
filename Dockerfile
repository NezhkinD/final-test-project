FROM budtmo/docker-android:emulator_14.0

RUN mkdir -p /tmp/apk && \
    wget -O /tmp/apk/wikipedia.apk \
    'https://f-droid.org/repo/org.wikipedia_50517.apk' && \
    chmod 644 /tmp/apk/wikipedia.apk
