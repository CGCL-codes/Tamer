    private static synchronized Tag open(String csChannel, String csRunId, Tag tagConfig) {
        LogFlowStd.declare();
        if (tagConfig != null) {
            LogCenters logCenters = new LogCenters();
            boolean b = logCenters.loadDefinition(csChannel, tagConfig, null);
            if (b) {
                Tag tagSettings = tagConfig.getChild("Settings");
                fillCallStack(tagSettings);
            }
            if (csChannel == null) {
                Vector<String> arrChannels = new Vector<String>();
                for (int i = 0; i < logCenters.getNbLogCenterloader(); i++) {
                    LogCenterLoader loader = logCenters.getLogCenterloader(i);
                    String ch = loader.getChannel();
                    if (!arrChannels.contains(ch)) {
                        arrChannels.add(ch);
                    }
                }
                for (String cs : arrChannels) {
                    setRunId(cs, csRunId);
                }
            } else {
                setRunId(csChannel, csRunId);
            }
        }
        return tagConfig;
    }
