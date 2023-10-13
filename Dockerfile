FROM wiremock/wiremock:3.0.4
COPY ./extensions /var/wiremock/extensions
ENTRYPOINT ["/docker-entrypoint.sh", "--global-response-templating", "--disable-gzip", "--local-response-templating"]