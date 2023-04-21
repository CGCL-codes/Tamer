    public void afterPropertiesSet() throws Exception {
        if (ArrayUtils.isEmpty(urls)) {
            throw new IllegalArgumentException("At least one server url must be set");
        }
        if (base != null && getJdkVersion().compareTo(JDK_142) <= 0) {
            throw new IllegalArgumentException("Base path is not supported for JDK versions < 1.4.2");
        }
        if (authenticationSource == null) {
            log.debug("AuthenticationSource not set - " + "using default implementation");
            if (StringUtils.isBlank(userName)) {
                log.warn("Property 'userName' not set - " + "anonymous context will be used for read-write operations");
            } else if (StringUtils.isBlank(password)) {
                log.warn("Property 'password' not set - " + "blank password will be used");
            }
            authenticationSource = new SimpleAuthenticationSource();
        }
        if (cacheEnvironmentProperties) {
            anonymousEnv = setupAnonymousEnv();
        }
    }
