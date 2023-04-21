    private void doListPresence(ListPresence msg) {
        try {
            ChannelData channelData = ChannelData.getChannelData(msg.channelName);
            Result result = Result.UNKNOWN;
            StringBuffer list = new StringBuffer();
            if (channelData == null) {
                result = Result.NOT_FOUND;
            } else {
                String ls = System.getProperty("line.separator");
                list.append("result: ");
                for (Presence p : channelData.getPresences()) {
                    list.append(ls);
                    list.append(p.getAccount());
                }
                result = Result.OK;
            }
            send(new ResultResponse(new Result(result.getCode(), list.toString())));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
