    GAEChannel(ServletConfig servletConfig, GravityConfig gravityConfig, String id) {
        if (id == null) throw new NullPointerException("id cannot be null");
        this.id = id;
        this.expiration = gravityConfig.getChannelIdleTimeoutMillis();
    }
