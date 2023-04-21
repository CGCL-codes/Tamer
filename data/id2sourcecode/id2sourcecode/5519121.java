    private void wsPostTrace(boolean isTop, boolean isFin, String contentType, ByteBuffer[] message) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(handler.getChannelId());
        sb.append(":");
        sb.append(onMessageCount);
        if (isTop) {
            onMessageCount++;
            sb.append(":top");
        }
        if (isFin) {
            sb.append(":fin");
        }
        sb.append(']');
        wsTrace(AccessLog.SOURCE_TYPE_WS_ON_MESSAGE, contentType, sb.toString(), "B<S", message);
    }
