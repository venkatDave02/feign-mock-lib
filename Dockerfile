FROM wiremock/wiremock:3.0.4
RUN mkdir -p /var/wiremock/extensions
COPY ./build/libs/*.jar /var/wiremock/extensions
ENTRYPOINT ["/docker-entrypoint.sh", "--global-response-templating", "--disable-gzip", "--local-response-templating"]