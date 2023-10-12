# Feign Mock library

> Feign mock library provides an abstract configuration to mock the down-stream services using wire-mock framework

---

Library that abstracts the mock server configuration to mock the down-streams.

- [WireMock documentation](https://wiremock.org/docs/)
- [WireMock Java Configuaraion](https://wiremock.org/docs/configuration/)
- Downstream services can be mocked using WireMock framework
- Downstream APIs mock responses are maintained under (__files/fixtures)
- All Downstream APIs response and common response are mocked and maintained under default directory
- Developers can create new directory for different use cases and create a mock response specific to the use case.