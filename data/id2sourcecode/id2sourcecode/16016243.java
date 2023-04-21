        static OutboundMux create(Connection c) throws IOException {
            logger.log(Level.FINEST, "opened {0}", c);
            OutboundMux mux = null;
            try {
                mux = (c.getChannel() == null) ? new OutboundMux(c) : new OutboundMux(c, true);
            } finally {
                if (mux == null) {
                    try {
                        c.close();
                    } catch (IOException e) {
                    }
                }
            }
            return mux;
        }
