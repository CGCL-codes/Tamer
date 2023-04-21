        void register(MsgContext ep) {
            Socket s = (Socket) ep.getNote(socketNote);
            try {
                s.getChannel().register(selector, SelectionKey.OP_READ, this);
            } catch (IOException iex) {
                log.error("Unable to register connection", iex);
                unregister(ep);
            }
        }
