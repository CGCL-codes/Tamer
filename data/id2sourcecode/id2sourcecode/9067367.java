    protected final synchronized void setCurrentData(D data) {
        getCurrentData().add(data.getChannel(), data);
    }
