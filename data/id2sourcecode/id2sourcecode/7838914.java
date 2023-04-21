    public ConfigBean getClone() {
        logger.debug("getClone()");
        return new ConfigBean(this.isCachable, this.mainEntity, this.relaEntitys, this.readOnlyMethods, this.writeMethods);
    }
