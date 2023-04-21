        public void operationComplete(ChannelFuture arg0) throws Exception {
            Channels.close(arg0.getChannel());
            FtpChannelUtils.teminateServer(configuration);
        }
