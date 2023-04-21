                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("Handshake: " + future.isSuccess(), future.getCause());
                        if (future.isSuccess()) {
                            setStatusSslConnectedChannel(future.getChannel(), true);
                        } else {
                            setStatusSslConnectedChannel(future.getChannel(), false);
                            future.getChannel().close();
                        }
                    }
