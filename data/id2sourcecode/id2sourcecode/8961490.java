        public GroupChannel createChannel() {
            channel = new GroupChannel();
            ((ReceiverBase) channel.getChannelReceiver()).setAutoBind(100);
            interceptor = new NonBlockingCoordinator() {

                public void fireInterceptorEvent(InterceptorEvent event) {
                    status = event.getEventTypeDesc();
                    int type = event.getEventType();
                    boolean display = VIEW_EVENTS[type];
                    if (display) parent.printScreen();
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (Exception x) {
                    }
                }
            };
            channel.addInterceptor(interceptor);
            channel.addInterceptor(new TcpFailureDetector());
            channel.addInterceptor(new MessageDispatch15Interceptor());
            return channel;
        }
