        public void onFinished() {
            logger.debug("#finished server.id:" + getChannelId());
            if (!isConnected || !isHandshaked) {
                client.completeResponse("500", "fail to connect");
            } else {
                isConnected = false;
                client.responseEnd();
            }
            super.onFinished();
        }
