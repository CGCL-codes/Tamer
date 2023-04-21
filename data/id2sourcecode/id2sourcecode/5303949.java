    public Log getLog(String category) {
        return new EnhydraLog(Logger.getCentralLogger().getChannel(category));
    }
