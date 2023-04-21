                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (!f.isSuccess()) {
                        log.warn("SSL handshake with " + e.getChannel().getRemoteAddress() + " failed!  Closing channel.");
                        e.getChannel().close();
                        return;
                    }
                    listener.linkEstablished(link, client);
                    connectfp.getManager().completeFuture(link);
                }
