    @Override
    public void messageReceived(ChannelHandlerContext arg0, MessageEvent arg1) throws Exception {
        try {
            long curtime = System.currentTimeMillis();
            long size = objectSizeEstimator.estimateSize(arg1.getMessage());
            if (trafficCounter != null) {
                trafficCounter.bytesRecvFlowControl(arg0, size);
                if (readLimit == 0) {
                    return;
                }
                long wait = getTimeToWait(readLimit, trafficCounter.getCurrentReadBytes(), trafficCounter.getLastTime(), curtime);
                if (wait > MINIMAL_WAIT) {
                    Channel channel = arg0.getChannel();
                    if (channel != null && channel.isConnected()) {
                        if (executor == null) {
                            if (release.get()) {
                                return;
                            }
                            Thread.sleep(wait);
                            return;
                        }
                        if (arg0.getAttachment() == null) {
                            arg0.setAttachment(Boolean.TRUE);
                            channel.setReadable(false);
                            executor.execute(new ReopenRead(arg0, wait));
                        } else {
                            if (release.get()) {
                                return;
                            }
                            Thread.sleep(wait);
                        }
                    } else {
                        if (release.get()) {
                            return;
                        }
                        Thread.sleep(wait);
                    }
                }
            }
        } finally {
            super.messageReceived(arg0, arg1);
        }
    }
