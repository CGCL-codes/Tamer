    protected Map getChannelMap(ConsoleInput ci) {
        Map channel_map = new HashMap();
        PluginInterface[] pis = ci.azureus_core.getPluginManager().getPluginInterfaces();
        for (int i = 0; i < pis.length; i++) {
            LoggerChannel[] logs = pis[i].getLogger().getChannels();
            if (logs.length > 0) {
                if (logs.length == 1) {
                    channel_map.put(pis[i].getPluginName(), logs[0]);
                } else {
                    for (int j = 0; j < logs.length; j++) {
                        channel_map.put(pis[i].getPluginName() + "." + logs[j].getName(), logs[j]);
                    }
                }
            }
        }
        return (channel_map);
    }
