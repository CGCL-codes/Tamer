    public void openTargetConnection(String url) throws MessagingException {
        this.targetStore = this.openConnection(url);
    }
