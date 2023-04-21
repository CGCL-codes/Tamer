            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                int rc;
                Queue<GenericCallback<Void>> oldPendingOps;
                synchronized (PerChannelBookieClient.this) {
                    if (future.isSuccess()) {
                        LOG.info("Successfully connected to bookie: " + addr);
                        rc = BKException.Code.OK;
                        channel = future.getChannel();
                        connected = true;
                    } else {
                        LOG.error("Could not connect to bookie: " + addr);
                        rc = BKException.Code.BookieHandleNotAvailableException;
                        channel = null;
                        connected = false;
                    }
                    connectionAttemptInProgress = false;
                    PerChannelBookieClient.this.channel = channel;
                    oldPendingOps = pendingOps;
                    pendingOps = new ArrayDeque<GenericCallback<Void>>();
                }
                for (GenericCallback<Void> pendingOp : oldPendingOps) {
                    pendingOp.operationComplete(rc, null);
                }
            }
