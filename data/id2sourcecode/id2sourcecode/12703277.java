    protected void setNewContextFactory() {
        env = (Map) getProcessEnvironment().clone();
        addNewContextFactory();
        ContextServer contextServer = getContextServer();
        AbstractChannelName channelName = contextServer.getChannelName(ctx);
        if (channelName != null) {
            env.put("X_JAVABRIDGE_REDIRECT", channelName.getName());
            ctx.getBridge();
            contextServer.start(channelName, Util.getLogger());
        }
        setStandardEnvironmentValues(env);
    }
