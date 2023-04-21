    public synchronized void eventTriggered(JamudEvent event) {
        if (state < STATE_INITIALIZED) {
            return;
        }
        if (event instanceof ChannelEvent) {
            ChannelEvent c = (ChannelEvent) event;
            this.println(c.getChannel().parseDisplay(c.getSource(), c.getText()));
            this.ready();
        } else if (event instanceof TellEvent) {
            TellEvent t = (TellEvent) event;
            this.println(t.getSource().getName().concat(" tells you, \"").concat(t.getText()).concat("\""));
            this.ready();
        } else if (event instanceof PrintEvent) {
            String text = ((PrintEvent) event).getText();
            this.print(text);
            this.ready();
        }
    }
