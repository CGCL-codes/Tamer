    public final void handleInternalEvent(Event ev, EventArgs args) {
        if (ev == Event.IRC_JOIN) {
            if (CHANNEL_TRACKING) {
                if (args.getParamAsString("nick").equalsIgnoreCase(getNick())) {
                    _channels.add((Channel) addEventListener(new Channel(args.getParamAsString("channel"))));
                }
            }
        } else if (ev == Event.IRC_PART) {
            if (CHANNEL_TRACKING) {
                if (args.getParamAsString("nick").equalsIgnoreCase(getNick())) {
                    String chanName = args.getParamAsString("channel");
                    if (!_channels.remove(getChannelForName(chanName))) {
                        throw new SnipesException("Channel was not in the lists, yet we parted it.");
                    }
                }
            }
        } else if (ev == Event.IRC_NICKINUSE && (Boolean) args.getParam("fatal")) {
            System.err.println("Error. Nickname already in use. Please make sure no other instances are running and restart this application.");
            System.exit(3);
        } else {
            System.err.println("Internal event handler: Unknown internal event " + ev + ".");
        }
    }
