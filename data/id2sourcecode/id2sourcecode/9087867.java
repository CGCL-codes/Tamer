        @Override
        public void run() {
            try {
                _listener.process(_event);
            } catch (RuntimeException e) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new EventDispatchingEvent(_event, _listener, e));
                } catch (ModuleNotFoundException e1) {
                }
            }
        }
