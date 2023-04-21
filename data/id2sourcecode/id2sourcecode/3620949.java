    public synchronized void start() throws LifecycleException {
        if (this.started) return;
        super.addLifecycleListener(this);
        try {
            CatalinaCluster catclust = (CatalinaCluster) this.getCluster();
            if (this.context == null) this.context = new ReplApplContext(this.getBasePath(), this);
            if (catclust != null) {
                ReplicatedMap map = new ReplicatedMap(this, catclust.getChannel(), DEFAULT_REPL_TIMEOUT, getName(), getClassLoaders());
                map.setChannelSendOptions(mapSendOptions);
                ((ReplApplContext) this.context).setAttributeMap(map);
                if (getAltDDName() != null) context.setAttribute(Globals.ALT_DD_ATTR, getAltDDName());
            }
            super.start();
        } catch (Exception x) {
            log.error("Unable to start ReplicatedContext", x);
            throw new LifecycleException("Failed to start ReplicatedContext", x);
        }
    }
