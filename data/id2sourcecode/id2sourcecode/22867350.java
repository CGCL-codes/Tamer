        static void create(ServerConnection c, RequestDispatcher dispatcher) {
            RequestDispatcher d = new Dispatcher(dispatcher, c);
            try {
                if (c.getChannel() == null) {
                    new InboundMux(c, d).start();
                } else {
                    new InboundMux(c, d, true).start();
                }
            } catch (IOException e) {
                if (logger.isLoggable(Levels.HANDLED)) {
                    logThrow(logger, "handleConnection", "{0} throws", new Object[] { c }, e);
                }
                try {
                    c.close();
                } catch (IOException ee) {
                }
            }
        }
