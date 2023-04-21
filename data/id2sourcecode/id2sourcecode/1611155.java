                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    writeFuture = getRemoteFuture().getChannel().write(chunk);
                }
