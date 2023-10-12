/* Copyright (C)2023 The Big Red Group */
package au.com.thebigredgroup.feign.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static au.com.thebigredgroup.feign.mock.Constants.*;

@Slf4j
public class MockConfigServer {

    private static final UUID uuid = UUID.randomUUID();

    private static WireMockServer wireMockServer = null;

    public static String resourcePath = DEFAULT_RESOURCE_PATH;

    public MockConfigServer(MockConfiguration mockConfig){
        resourcePath = isEmpty(mockConfig.getResourcePath()) ? DEFAULT_RESOURCE_PATH : mockConfig.getResourcePath();
        log.info("Resource base path: {}", resourcePath);
        wireMockServer = new WireMockServer(wireMockConfig()
                .notifier(new ConsoleNotifier(mockConfig.getVerbose()))
                .extensions(ResponseTransformer.class.getName())
                .globalTemplating(true)
                .templatingEnabled(true)
                .withRootDirectory(resourcePath)
                .usingFilesUnderClasspath(resourcePath)
                .port(mockConfig.getPort() == null ? wireMockConfig().dynamicPort().portNumber() : mockConfig.getPort()));
    }

    public String start(){
        return startMockServer();
    }

    public void stop(){
        if(wireMockServer != null) {
            wireMockServer.stop();
        }else {
            throw new RuntimeException("Failed to stop mock server");
        }
    }

    public static void stub(String resource, boolean... isEdit){
        boolean editStub = isEdit.length == 0 || isEdit[0];
        MappingBuilder mappingBuilder = any(urlMatching(ANY))
                .withId(uuid)
                .willReturn(ok()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withTransformerParameters(Map.of(USE_CASE, !isEmpty(resource) ? resource : DEFAULT,
                                RESOURCE_PATH, resourcePath))
                        .withTransformers(RESPONSE_TRANSFORMER));
        if(editStub){
            wireMockServer.editStub(mappingBuilder);
        }else{
            wireMockServer.stubFor(mappingBuilder);
        }
        Map.of(USE_CASE, !isEmpty(resource) ? resource : DEFAULT,
                RESOURCE_PATH, resourcePath);
    }

    private String startMockServer(){
        if(wireMockServer != null) {
            try {
                wireMockServer.start();
            }catch (Exception exception){
                throw new RuntimeException("Failed to start mock server: "+ exception.getMessage());
            }
        }else {
            throw new RuntimeException("Failed to start mock server");
        }
        String wireMockBaseUrl = wireMockServer.baseUrl();
        log.info("Mock server started at: {}", wireMockBaseUrl);
        stub(DEFAULT, false);
        return wireMockBaseUrl;
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

}
