    public AppContextImpl(final Object context) throws SecurityException, NoSuchMethodException, NoSuchFieldException {
        this.fAppContext = context;
        final Class<?> clazz = this.fAppContext.getClass();
        this.fGetChannelManager = clazz.getDeclaredMethod("getChannelManager");
        this.fGetDataManager = clazz.getDeclaredMethod("getDataManager");
        this.fGetTaskManager = clazz.getDeclaredMethod("getTaskManager");
        this.fGetManager = clazz.getDeclaredMethod("getManager", Class.class);
        this.fGetService = clazz.getDeclaredMethod("getService", Class.class);
        this.fServices = clazz.getDeclaredField("serviceComponents");
        this.fManagers = clazz.getDeclaredField("managerComponents");
    }
