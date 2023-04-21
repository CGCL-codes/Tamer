        @Override
        public void operationComplete(ChannelFuture aFuture) throws Exception {
            if (aFuture.isSuccess()) {
                if (mLog.isInfoEnabled()) {
                    mLog.info("SSL handshaking success");
                }
            } else {
                aFuture.getChannel().close();
            }
        }
