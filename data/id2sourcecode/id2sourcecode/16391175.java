        public void operationComplete(ChannelFuture future) {
            MUSUser whatUser = ((SMUSPipeline) future.getChannel().getPipeline()).user;
            if (future.isSuccess()) {
            } else {
                MUSLog.Log("Close failure for " + whatUser + ": " + future.getCause(), MUSLog.kDeb);
                whatUser.m_scheduledToDie = false;
                future.getChannel().close();
            }
        }
