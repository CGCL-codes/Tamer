    public void setSettings(Settings settings) {
        this.settings.setDevice(settings.getDevice());
        this.settings.setBaudRate(settings.getBaudRate());
        this.settings.setDataBits(settings.getDataBits());
        this.settings.setStopBits(settings.getStopBits());
        this.settings.setParity(settings.getParity());
        this.settings.setPrecision(settings.getPrecision());
        this.settings.setChannels(settings.getChannels());
        this.settings.setStartCommand(settings.getStartCommand());
        this.settings.setStopCommand(settings.getStopCommand());
        this.settings.setCommandFormat(settings.getCommandFormat());
        this.settings.setResolutionOfGraph(settings.getResolutionOfGraph());
    }
