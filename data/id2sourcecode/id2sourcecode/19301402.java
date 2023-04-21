        public boolean isMaximized(final Channel channel) {
            return (maximizedChannelKey != null) && (maximizedChannelKey.equals(ContentManager.ChannelSpecification.getKey(getChannelSpecification(channel))));
        }
