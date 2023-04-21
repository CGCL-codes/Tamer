        public URI getChannelLocalPath(final Channel channel) {
            if (channel == null) return channelLocalPath;
            return isMaximized(channel) ? channelLocalPath : null;
        }
