    public Reader(Selector source, Collector task) {
        this.channel = task.getChannel();
        this.source = source;
        this.task = task;
    }
