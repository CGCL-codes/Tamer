        public ClientHttpResponse execute() throws IOException {
            ClientHttpResponse response = delegate.execute();
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.info("Token is invalid (got 401 response). Trying get a new token using the refresh token");
                String newToken = callback.refreshToken();
                if (newToken == null) {
                    return response;
                } else {
                    logger.info("New token obtained, retrying the request with it");
                    for (ClientHttpRequestInterceptor interceptor : requestInterceptors) {
                        if (interceptor.getClass().getName().equals("org.springframework.social.oauth2.OAuth2RequestInterceptor")) {
                            Field field = ReflectionUtils.findField(interceptor.getClass(), "accessToken");
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, interceptor, newToken);
                        }
                    }
                    ClientHttpRequest newRequest = TokenRefreshingClientHttpRequestFactory.this.delegate.createRequest(delegate.getURI(), delegate.getMethod());
                    return newRequest.execute();
                }
            }
            return response;
        }
