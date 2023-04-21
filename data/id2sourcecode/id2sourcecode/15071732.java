    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splittedLine) throws Exception {
        if (splittedLine.length >= 2) {
            if (splittedLine[1].equalsIgnoreCase("*CLEAR")) {
                c.getChannelServer().setArrayString("");
                mc.dropMessage("Array Sucessfully Flushed");
            } else {
                c.getChannelServer().setArrayString(c.getChannelServer().getArrayString() + StringUtil.joinStringFrom(splittedLine, 1));
                mc.dropMessage("Added " + StringUtil.joinStringFrom(splittedLine, 1) + " to the array. Use !array to check.");
            }
        } else mc.dropMessage("Array: " + c.getChannelServer().getArrayString());
    }
