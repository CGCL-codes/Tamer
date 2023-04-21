        private OutboundMux(Connection c, boolean ignore) throws IOException {
            super(c.getChannel());
            this.c = c;
        }
