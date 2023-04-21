                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        sendContent(future.getChannel(), content);
                        connectFailedCount = 0;
                    } else {
                        connectFailedCount++;
                        if (connectFailedCount < 3) {
                            logger.error("Failed to connect remote c4 server, try again");
                            doSend(content);
                        } else {
                            connectFailedCount = 0;
                        }
                    }
                }
