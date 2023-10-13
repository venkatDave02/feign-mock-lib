# Feign Mock Library

> Feign mock library provides an abstract configuration to mock the downstream services using Wiremock framework
> - Library that abstracts mock server configuration to mock all downstream responses.
> - This Library uses Wiremock framework to mock the downstream endpoints.
>   - [WireMock documentation](https://wiremock.org/docs/)
---

### Sample Mock Server Configuration

Example 1:
``` java
MockConfigServer mockConfigServer = new MockConfigServer(mockConfig());
String mockServerBaseUrl = server.start();
```
- The above configuration initialize the mock server with default values
- By default, resourcePath sets to "src/test/resources"
- By default, verbose sets to false
- By default, port sets to wiremock's dynamic port number

Example 2:
``` java
MockConfigServer mockConfigServer = new MockConfigServer(mockConfig()
                .resourcePath("src/test/resources")
                .verbose(true)
                .port(8383));
String mockServerBaseUrl = server.start();
```
- Developers can override the mock server configuration as mentioned above


### How to override down-stream services

``` java
//capi-base-path: "http://localhost:5052"
System.setProperty("capi-base-path", mockServerBaseUrl.concat("/capi"));
//icapi-base-path: "http://localhost:5053"
System.setProperty("icapi-base-path", mockServerBaseUrl.concat("/icapi"));
```
- Developers can override the property value with the mock server base url as mentioned above

#### Folder structure to place all your downstream mock response

![resource-dir-structure.png](images%2Fresource-dir-structure.png)
- All the downstream mock response is placed under ("/__files/fixtures")
- Configure the resource base path (ex: "src/test/resources") in the MockConfigServer
  - Reference: Sample Mock Server Configuration -> Example 2
- Here, default and not-found directories are considered two different use cases
- All the successful use cases should be placed under the default directory,
  and failed use cases should be placed under the not-found directory

#### Downstream mock response directory structure

Example 1:

![downstream-response-dir-structure.png](images%2Fdownstream-response-dir-structure.png)
- Downstream CAPI endpoint (http://localhost:8383/capi/authentication/login)
- Consider this as a successful use-case so the response will be under the default directory
- Folder structure is associated with CAPI endpoint URI, starting from downstream service name (capi),
  goes till the end, and the last part of URI (/login) is considered as a file name (login.json)
  so our directory structure will look like -> /capi/authentication/login.json


#### How to switch directory for different use-cases

``` java
//default usecase
MockConfigServer.stub("default");
//not-found usecase
MockConfigServer.stub("not-found");
```
- Developers can switch between the use-case root dir by calling the stub method as mentioned above
- By default, MockConfigServer is configured with the default use-case
- In case the file is not found in the configured use-case dir, the lib will look for that file under the default directory
    - Example (not-found): If the file not found under (__files/fixtures/not-found/icapi/en/template/2/CATEGORY/526/cruses.json), then
      lib will look for the file under (__files/fixtures/default/icapi/en/template/2/CATEGORY/526/cruses.json)
      ![downstream-response-dir-structure-1.png](images%2Fdownstream-response-dir-structure-1.png)