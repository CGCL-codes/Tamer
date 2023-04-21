        public void itemFound(ItemIF newItem, ChannelIF existingChannel) {
            context.remove();
            context.set(newItem.getChannel());
            if (logger.isInfoEnabled()) {
                logger.info("New Item Found : " + existingChannel.getLocation());
            }
        }
