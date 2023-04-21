    public void init() {
        super.init();
        consoleChannel = Symbiosis.getChannelManager().getChannel(MessageConstants.CONSOLE_CHANNEL);
        consoleChannel.addMessageListener(this);
        splash = new SplashController();
        splash.start();
        currentScreen = SPLASH_SCREEN;
    }
