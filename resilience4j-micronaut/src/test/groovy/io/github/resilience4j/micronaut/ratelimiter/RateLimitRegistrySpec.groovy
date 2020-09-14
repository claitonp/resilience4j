package io.github.resilience4j.micronaut.ratelimiter


import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
@Property(name = "resilience4j.ratelimiter.enabled", value = "true")
@Property(name = "resilience4j.ratelimiter.configs.default.limitForPeriod", value = "10")
@Property(name = "resilience4j.ratelimiter.configs.default.limitRefreshPeriod", value = "PT1s")
@Property(name = "resilience4j.ratelimiter.configs.default.timeoutDuration", value = "PT3s")
@Property(name = "resilience4j.ratelimiter.instances.backendA.baseConfig", value = "default")
@Property(name = "resilience4j.ratelimiter.instances.backendB.baseConfig", value = "default")
@Property(name = "resilience4j.ratelimiter.instances.backendB.timeoutDuration", value = "PT5s")
class RateLimitRegistrySpec extends Specification {
    @Inject ApplicationContext applicationContext


    void "default configuration"() {
        given:
        def registry = applicationContext.getBean(RateLimiterRegistry)
        def defaultRateLimiter = registry.rateLimiter("default")

        expect:
        defaultRateLimiter != null

        defaultRateLimiter.rateLimiterConfig.limitForPeriod == 10
        defaultRateLimiter.rateLimiterConfig.limitRefreshPeriod.seconds == 1
        defaultRateLimiter.rateLimiterConfig.timeoutDuration.seconds == 3
        defaultRateLimiter.getName() == "default"
    }

    void "backend-a configuration"() {
        given:
        def registry = applicationContext.getBean(RateLimiterRegistry)
        def backendARateLimiter = registry.rateLimiter("backend-a")

        expect:
        backendARateLimiter != null

        backendARateLimiter.rateLimiterConfig.limitForPeriod == 10
        backendARateLimiter.rateLimiterConfig.limitRefreshPeriod.seconds == 1
        backendARateLimiter.rateLimiterConfig.timeoutDuration.seconds == 3
        backendARateLimiter.getName() == "backend-a"

    }

    void "backend-b configuration"() {
        given:
        def registry = applicationContext.getBean(RateLimiterRegistry)
        def backendBRateLimiter = registry.rateLimiter("backend-b")

        expect:
        backendBRateLimiter != null

        backendBRateLimiter.rateLimiterConfig.limitForPeriod == 10
        backendBRateLimiter.rateLimiterConfig.limitRefreshPeriod.seconds == 1
        backendBRateLimiter.rateLimiterConfig.timeoutDuration.seconds == 5
        backendBRateLimiter.getName() == "backend-b"
    }

}
