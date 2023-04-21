    public void exec() throws Reply501Exception, Reply500Exception {
        if (!getSession().getAuth().isAdmin()) {
            throw new Reply500Exception("Command Not Allowed");
        }
        if (!hasArg()) {
            getConfiguration().changeNetworkLimit(0, 0);
            getSession().setReplyCode(ReplyCode.REPLY_200_COMMAND_OKAY, "Limit reset to default");
            return;
        }
        String[] limits = getArgs();
        long writeLimit = 0;
        long readLimit = 0;
        try {
            if (limits.length == 1) {
                writeLimit = Long.parseLong(limits[0]);
                readLimit = writeLimit;
            } else {
                writeLimit = Long.parseLong(limits[0]);
                readLimit = Long.parseLong(limits[1]);
            }
        } catch (NumberFormatException e) {
            throw new Reply501Exception(getCommand() + " ([write and read limits in b/s] | [write limit in b/s] [read limit in b/s]");
        }
        getConfiguration().changeNetworkLimit(writeLimit, readLimit);
        getSession().setReplyCode(ReplyCode.REPLY_200_COMMAND_OKAY, "Limit set to new values");
    }
