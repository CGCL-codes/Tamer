    public void warnVersionIfNeeded(Player p) {
        if (new VersionComparator().compare(GameEngineThread.getVersionNumber(), parserVersion) < 0) {
            if (p == null) {
                writeError(UIMessages.getInstance().getMessage("age.version.warning.header") + "\n");
                writeError(UIMessages.getInstance().getMessage("age.version.warning.2", "$curversion", GameEngineThread.getVersionNumber(), "$reqversion", parserVersion));
                writeError(" ");
                writeError(UIMessages.getInstance().getMessage("age.download.url") + "\n\n");
            } else {
                p.writeError(UIMessages.getInstance().getMessage("age.version.warning.header") + "\n");
                p.writeError(UIMessages.getInstance().getMessage("age.version.warning.2", "$curversion", GameEngineThread.getVersionNumber(), "$reqversion", parserVersion));
                p.writeError(" ");
                p.writeError(UIMessages.getInstance().getMessage("age.download.url") + "\n\n");
                p.waitKeyPress();
            }
        }
    }
