    private void wsCloseTrace(short code, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ServerClose:");
        sb.append(handler.getChannelId());
        sb.append(":code:");
        sb.append(code);
        sb.append(":reason:");
        sb.append(reason);
        sb.append(']');
        wsTrace(AccessLog.SOURCE_TYPE_WS_ON_MESSAGE, null, sb.toString(), "B<S", null);
    }
