package com.github.jasjisdo.directory_watch_service.service;

/**
 * Interface definition for services.
 */
public interface Service {
    /**
     * Starts the service. This method blocks until the service has completely started.
     */
    void start() throws Exception;

    /**
     * Stops the service. This method blocks until the service has completely shut down.
     */
    void stop();
}