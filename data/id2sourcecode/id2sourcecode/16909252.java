    public void handle_command(Service who, String user, String replyto, String arguments) {
        if (((SrvChannel) who).getChannels().containsKey(arguments.toLowerCase())) {
            SrvChannel_channel t = ((SrvChannel) who).getChannels().get(arguments.toLowerCase());
            Generic.curProtocol.outNOTICE(who, user, "Channel: " + arguments);
            Generic.curProtocol.outNOTICE(who, user, "--------- ");
            if (t.getAllMeta().containsKey("_ts_registered")) Generic.curProtocol.outNOTICE(who, user, "Registered On: " + DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date(Integer.parseInt(t.getMeta("_ts_registered")) * 1000L))); else Generic.curProtocol.outNOTICE(who, user, "Unknown Registration Date.");
            if (t.getAllMeta().containsKey("_ts_last")) Generic.curProtocol.outNOTICE(who, user, "Last had a user: " + DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date(Integer.parseInt(t.getMeta("_ts_last")) * 1000L))); else Generic.curProtocol.outNOTICE(who, user, "Has never had a user.");
            if (t.getAllMeta().containsKey("staffnote")) Generic.curProtocol.outNOTICE(who, user, "Staff Note: " + t.getMeta("staffnote")); else Generic.curProtocol.outNOTICE(who, user, "Has no staff note.");
            if (t.getAllMeta().containsKey("_isbad")) Generic.curProtocol.outNOTICE(who, user, "Is a naughty, naughty channel!");
            if (t.getAllMeta().size() == 0) Generic.curProtocol.outNOTICE(who, user, "Has no Metadata."); else Generic.curProtocol.outNOTICE(who, user, "Channel Metadata:");
            for (Map.Entry<String, String> e : t.getAllMeta().entrySet()) Generic.curProtocol.outNOTICE(who, user, "    " + util.pad(e.getKey(), 14) + ": " + e.getValue());
            if (t.getUsers().size() == 0) Generic.curProtocol.outNOTICE(who, user, "Has no users."); else Generic.curProtocol.outNOTICE(who, user, "Channel Users:");
            for (Map.Entry<String, SrvChannel_channel.ChanAccess> e : t.getUsers().entrySet()) Generic.curProtocol.outNOTICE(who, user, "    " + util.pad(e.getKey(), 14) + ": " + e.getValue());
        } else Generic.curProtocol.outNOTICE(who, user, "Error: No such registered channel...");
    }
