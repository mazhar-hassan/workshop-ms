# Configure routes

## configure route for movies-service
I want to recognise API hits from Gatekeeper to movies-service by custom url mapping ([prefix])

Normal url is follows if we access movies service directly
> /api/movies

In order for our Gatekeeper (API Gateway) to differentiate based on a prefix, we will add filter in configuration

>/ms-movies/api/movies

ms-movies will be stripped as per the configuration and only /api/movies will be forwarded.

        - id: movies-service
          uri: lb://PTV-MOVIES
          predicates:
            - Path=/ms-movies/**
          filters:
            - StripPrefix=1