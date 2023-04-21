                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    connectLock.lock();
                    FutureManager<Void> fm = connectFuture.getManager();
                    connectFuture = null;
                    connectLock.unlock();
                    if (!future.isSuccess()) {
                        fm.cancelFuture(future.getCause());
                    } else {
                        channel = future.getChannel();
                        connected = true;
                        fm.completeFuture(null);
                    }
                }
