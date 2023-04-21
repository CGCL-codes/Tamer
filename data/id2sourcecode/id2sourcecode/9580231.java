                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        ChannelInitTask result = initConnectedChannel(future.getChannel(), request);
                        result.onVerify();
                    } else {
                        close();
                    }
                }
