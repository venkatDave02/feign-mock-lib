/* Copyright (C)2023 The Big Red Group */
package au.com.thebigredgroup.feign.mock;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.extension.responsetemplating.HandlebarsOptimizedTemplate;
import com.github.tomakehurst.wiremock.extension.responsetemplating.RequestTemplateModel;
import com.github.tomakehurst.wiremock.http.*;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static au.com.thebigredgroup.feign.mock.Constants.*;
import static com.github.tomakehurst.wiremock.common.ParameterUtils.getFirstNonNull;
import static com.github.tomakehurst.wiremock.extension.responsetemplating.TemplateEngine.defaultTemplateEngine;

public class ResponseTransformer implements ResponseDefinitionTransformerV2 {

    @Override
    public String getName() {
        return RESPONSE_TRANSFORMER;
    }

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        String fileResource = fetchFileResource(serveEvent);
        return new ResponseDefinitionBuilder()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                .withStatus(200)
                .withBodyFile(fileResource)
                .build();
    }

    private String fetchFileResource(ServeEvent serveEvent) {
        try {
            Parameters parameters = getFirstNonNull(serveEvent.getResponseDefinition().getTransformerParameters(), Parameters.empty());
            Map<String, Object> model = Map.of(
                    PARAMETERS, parameters,
                    REQUEST, RequestTemplateModel.from(serveEvent.getRequest(),
                            serveEvent.getStubMapping().getRequest().getUrlMatcher().getPathTemplate()));
            String useCase = parameters.get(USE_CASE) != null ? parameters.get(USE_CASE).toString() : DEFAULT;
            System.out.println("a.c.t.f.c.f.c.ResponseTransformer - Looking for use case: "+ useCase);
            String resourcePath = parameters.get(RESOURCE_PATH) != null ? parameters.get(RESOURCE_PATH).toString() : DEFAULT_RESOURCE_PATH;
            System.out.println("a.c.t.f.c.ResponseTransformer - Resource Path: "+ resourcePath);
            HandlebarsOptimizedTemplate filePathTemplate = defaultTemplateEngine().getUncachedTemplate(REQUEST_PATH);
            String compiledRequestPath = uncheckedApplyTemplate(filePathTemplate, model);
            System.out.println("a.c.t.f.c.ResponseTransformer - Compiled request path: "+ compiledRequestPath);
            String useCaseFilePath = resourcePath.concat(FILES).concat(PATH_PREFIX).concat(useCase).concat(compiledRequestPath).concat(JSON);
            System.out.println("a.c.t.f.c.ResponseTransformer - Use case file path: " + useCaseFilePath);
            boolean isFileExists = Files.exists(Paths.get(useCaseFilePath));
            System.out.println("a.c.t.f.c.ResponseTransformer - Is use case file path exists: " + isFileExists);
            String finalPath = FIXTURES.concat(isFileExists ? useCase : DEFAULT)
                    .concat(compiledRequestPath)
                    .concat(JSON);
            System.out.println("a.c.t.f.c.ResponseTransformer - File resource path: " + finalPath);
            return finalPath;
        }catch (Exception exception){
            System.err.println("a.c.t.f.c.ResponseTransformer - ".concat(ERROR).concat(FAILED_TO_FETCH_FILE_RESOURCE).concat(exception.getMessage()));
            return FIXTURES.concat(DEFAULT).concat(REQUEST_PATH).concat(JSON);
        }
    }

    private String uncheckedApplyTemplate(HandlebarsOptimizedTemplate template, Object context) {
        return template.apply(context);
    }

}
