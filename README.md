# Feign Mock Library

> Feign mock library provides an abstract configuration to mock the downstream services (external APIs)
> - This library abstracts mock server configuration to mock downstream responses.
> - This library uses the Wiremock framework to mock the downstream services.
>   - [WireMock documentation](https://wiremock.org/docs/)
---

### Sample Mock Server Configuration

Example 1:
``` java
MockConfigServer mockConfigServer = new MockConfigServer(mockConfig());
String mockServerBaseUrl = server.start();
```
- The above configuration initializes the mock server with default values
- By default, the resource path (resource base path) is set to "src/test/resources"
- By default, verbose (enable to log request and response info) sets to false
- By default, port (configure to run config server in specific port) is set to wire mock's dynamic port number

Example 2:
``` java
MockConfigServer mockConfigServer = new MockConfigServer(mockConfig()
                .resourcePath("src/test/resources")
                .verbose(true)
                .port(8383));
String mockServerBaseUrl = server.start();
```
- Developers can override the mock server configuration as mentioned above


#### How to override downstream services

``` java
//capi-base-path: "http://localhost:5052"
System.setProperty("capi-base-path", mockServerBaseUrl.concat("/capi"));
//icapi-base-path: "http://localhost:5053"
System.setProperty("icapi-base-path", mockServerBaseUrl.concat("/icapi"));
```
- Developers can override the property value with the mock server base URL as mentioned above

#### Folder structure to place all your downstream mock response

- All the downstream mock response is placed under ("/__files/fixtures")
- Configure the resource base path (ex: "src/test/resources") in the MockConfigServer
  - Reference: Sample Mock Server Configuration -> Example 2
- Here, default and not-found directories are considered two different use cases
- All the successful use cases should be placed under the default directory,
  and failed use cases should be placed under the not-found directory

![resource-dir-structure.jpg](assets%2Fimages%2Fresource-dir-structure.jpg)

#### Downstream mock response directory structure

Example 1:

- Consider CAPI is the Downstream endpoint (http://localhost:8383/capi/authentication/login)
- Consider this as a successful use-case so the response goes under the default directory
- Folder structure is associated with CAPI endpoint URI, starting from the service name (capi),
  goes till the end, and the last part of URI (/login) is considered as a file name (login.json)
  so our directory structure will look like -> /capi/authentication/login.json

![downstream-response-dir-structure.jpg](assets%2Fimages%2Fdownstream-response-dir-structure.jpg)

#### How to switch directories for different use cases

``` java
//default use case
MockConfigServer.stub("default");
//not-found use case
MockConfigServer.stub("not-found");
```
- Developers can switch between the use-case root dir by calling the stub method as mentioned above
- By default, MockConfigServer is configured with the default use-case
- In case the file is not found in the configured use-case dir, the lib will look for that file under the default directory
    - Example (not-found): If the file not found under (__files/fixtures/not-found/icapi/en/template/2/CATEGORY/526/cruses.json), then
      lib will look for the file under (__files/fixtures/default/icapi/en/template/2/CATEGORY/526/cruses.json)
      ![downstream-response-dir-structure-1.jpg](assets%2Fimages%2Fdownstream-response-dir-structure-1.jpg)

