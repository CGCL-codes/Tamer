    public void tracePingPong(String pingPong) {
        if (logType != LogType.TRACE) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(pingPong);
        sb.append(":");
        sb.append(handler.getChannelId());
        sb.append(']');
        wsTrace(AccessLog.SOURCE_TYPE_WS_POST_MESSAGE, null, sb.toString(), "B>S", null);
    }
