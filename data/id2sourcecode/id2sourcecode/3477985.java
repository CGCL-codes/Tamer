                        @Override
                        public void run(Timeout timeout) {
                            Channels.fireMessageReceived(future.getChannel(), writeNext);
                        }
