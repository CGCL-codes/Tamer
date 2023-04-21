    public CometdBasePage(final PageParameters parameters) {
        final String userchannel = WicketCometdSession.get().getCometUser();
        getChannelService().addChannelListener(this, userchannel, new RemoteListener());
    }
