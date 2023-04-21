    public final Channel findChannel(String name) {
        return ScriptVars.curConnection.getChannel(name);
    }
