/* Copyright (C)2023 The Big Red Group */
package au.com.thebigredgroup.feign.mock;

public class MockConfiguration {

    private boolean verbose;
    private String resourcePath;
    private Integer port;

    private MockConfiguration(){}

    public static MockConfiguration mockConfig() {
        return new MockConfiguration();
    }

    public MockConfiguration verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public MockConfiguration resourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }

    public MockConfiguration port(Integer port) {
        this.port = port;
        return this;
    }

    public boolean getVerbose(){
        return this.verbose;
    }

    public String getResourcePath(){
        return this.resourcePath;
    }

    public Integer getPort(){
        return this.port;
    }
}
