        public void onFinished() {
            logger.debug("#finished.cid:" + getChannelId());
            isConnected = false;
            client.asyncClose(null);
            client.unref();
        }
