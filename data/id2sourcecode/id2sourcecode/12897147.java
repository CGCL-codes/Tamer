            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("Handshake: " + future.isSuccess(), future.getCause());
                if (future.isSuccess()) {
                    if (isShutdown(future.getChannel())) {
                        answered = true;
                        return;
                    }
                    answered = false;
                    factory.addChannel(future.getChannel());
                } else {
                    answered = true;
                    future.getChannel().close();
                }
            }
