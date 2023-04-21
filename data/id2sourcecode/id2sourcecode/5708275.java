    public BackupApplication(IApplicationContext context) {
        this.context = context;
        this.channelConfig = context.getConfiguration();
        this.root = new File(context.getDataDirectory());
        this.host = context.getChannelURL().getHost();
    }
