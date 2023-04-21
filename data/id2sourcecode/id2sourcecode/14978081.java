    @Override
    public void setReadTimeout(final int timeMillis) throws IOException {
        final SocketChannel channel = getChannel();
        if (channel != null) NetUtil.setReadTimeout(channel, timeMillis); else if (timeMillis < 0) throw new SocketException(getClass().getName() + "#setReadTimeout(" + timeMillis + ") bad value");
        _readTimeout = timeMillis;
    }
