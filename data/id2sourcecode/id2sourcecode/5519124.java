    private void wsOnCloseTrace(short code, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("[BrowserClose:");
        sb.append(handler.getChannelId());
        sb.append(":code:");
        sb.append(code);
        sb.append(":reason:");
        sb.append(reason);
        sb.append(']');
        wsTrace(AccessLog.SOURCE_TYPE_WS_POST_MESSAGE, null, sb.toString(), "B>S", null);
    }
