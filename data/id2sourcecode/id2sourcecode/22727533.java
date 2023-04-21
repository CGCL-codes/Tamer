    public final void loadThread(PircBot _b, org.retro.gis.NewQueue q) {
        _bot = _b;
        _msgQueue = q;
        _channel = null;
        String[] str = _bot.getChannels();
        if (str != null) {
            if (str.length > 0) {
                _channel = str[0];
            } else {
                _channel = _bot.getAttemptedChannel();
            }
        }
        miscSetup();
    }
