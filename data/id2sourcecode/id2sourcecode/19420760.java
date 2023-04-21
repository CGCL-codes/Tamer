    public CometdRemote(PageParameters parameters) {
        super(parameters);
        add(HeaderContributor.forJavaScript(CometdAppBehavior.class, "js/cometd-app.js"));
        add(new RedirectUrlBehavior());
        final CometdService s = (CometdService) getCometdService();
        s.addChannelRemoveListener("chat", new RemoveListener() {

            public void removed(final String clientId, final boolean timeout) {
                final ChannelEvent event = new ChannelEvent("chat");
                event.addData("message", clientId + "just left");
                getChannelService().publish(event);
            }
        });
    }
