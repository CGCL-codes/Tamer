        public void onFailure(Object userContext, Throwable t) {
            logger.debug("#failure server.id:" + getChannelId(), t);
            server.asyncClose(userContext);
        }
