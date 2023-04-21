        private InboundMux(ServerConnection c, RequestDispatcher dispatcher, boolean ignore) throws IOException {
            super(c.getChannel(), dispatcher);
            this.c = c;
        }
