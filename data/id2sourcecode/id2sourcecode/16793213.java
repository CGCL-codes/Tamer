        public void onFailure(Object userContext, Throwable t) {
            logger.debug("#failure.cid:" + getChannelId(), t);
            server.asyncClose(userContext);
        }
