    private void wsOnTrace(boolean isTop, boolean isFin, String contentType, ByteBuffer[] message) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(handler.getChannelId());
        sb.append(":");
        sb.append(postMessageCount);
        if (isTop) {
            postMessageCount++;
            sb.append(":top");
        }
        if (isFin) {
            sb.append(":fin");
        }
        sb.append(']');
        wsTrace(AccessLog.SOURCE_TYPE_WS_POST_MESSAGE, contentType, sb.toString(), "B>S", message);
    }
