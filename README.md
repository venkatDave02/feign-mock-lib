# Feign Mock Library

> Feign mock library provides an abstract configuration to mock the down-stream services using wire-mock framework

---

Library that abstracts the mock server configuration to mock the down-streams.

This Library use Wiremock framework to mock the down-streams.

- [WireMock documentation](https://wiremock.org/docs/)
- [WireMock Java Configuaraion](https://wiremock.org/docs/configuration/)
- Downstream APIs mock responses are maintained under (__files/fixtures)
- All Downstream APIs response and common response are mocked and maintained under default directory
- Developers can create new directory for different use cases and create a mock response specific to the use case.

Sample Configuration

Example 1:
``` java
MockConfigServer mockConfigServer = new MockConfigServer(mockConfig());
String mockServerBaseUrl = server.start();
```
- The above configuration initialize the mock server with default values
- The resourcePath sets to "src/test/resources" by default
- The verbose sets to false by default
- The port sets to wiremock's dynamic port number

Example 2:
``` java
MockConfigServer mockConfigServer = new MockConfigServer(mockConfig()
                .resourcePath("src/test/resources")
                .verbose(true)
                .port(8383));
String mockServerBaseUrl = server.start();
```
- Developer can override the configuration as mentioned above
