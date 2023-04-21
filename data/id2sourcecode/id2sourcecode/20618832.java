        public void operationComplete(ChannelFuture future) {
            logger.debug("SSL remover");
            waitForSsl.remove(future.getChannel().getId());
        }
