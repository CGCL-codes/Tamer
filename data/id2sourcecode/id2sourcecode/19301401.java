        public ContentManager.ChannelSpecification<?> getChannelSpecification(final Channel channel) {
            return (channel != null) ? definition.getChannelSpecification(channel.getDefinition().getID()) : null;
        }
