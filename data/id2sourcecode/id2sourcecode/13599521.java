    public String[] getAccessWords() {
        if (access != null) return access;
        access = CMLib.channels().getChannelNames();
        if (access != null) {
            for (int i = 0; i < access.length; i++) if (access[i].equalsIgnoreCase("AUCTION")) access[i] = "";
        }
        return access;
    }
