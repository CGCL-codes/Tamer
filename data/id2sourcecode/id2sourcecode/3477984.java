            @Override
            public void operationComplete(ChannelFuture cf) {
                final long completedIn = System.currentTimeMillis() - writeTime;
                if (completedIn > 1000) {
                    logger.warn("channel busy? time taken to write last message: {}", completedIn);
                }
                final long delayToUse = delay - completedIn;
                if (delayToUse > RtmpConfig.TIMER_TICK_SIZE) {
                    timer.newTimeout(new TimerTask() {

                        @Override
                        public void run(Timeout timeout) {
                            Channels.fireMessageReceived(future.getChannel(), writeNext);
                        }
                    }, delayToUse, TimeUnit.MILLISECONDS);
                } else {
                    Channels.fireMessageReceived(future.getChannel(), writeNext);
                }
            }
