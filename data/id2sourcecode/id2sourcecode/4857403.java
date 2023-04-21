        public boolean postFrame(SessionImpl s, Frame f) throws BEEPException {
            synchronized (s) {
                return ((ChannelImpl) f.getChannel()).postFrame(f);
            }
        }
