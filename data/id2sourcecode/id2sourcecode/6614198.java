    public ConfigBean(boolean isCachable, String mainEntity, String[] relaEntitys, String[] readOnlyMethods, String[] writeMethods) {
        this.isCachable = isCachable;
        this.mainEntity = mainEntity;
        this.relaEntitys = relaEntitys;
        this.readOnlyMethods = readOnlyMethods;
        this.writeMethods = writeMethods;
    }
