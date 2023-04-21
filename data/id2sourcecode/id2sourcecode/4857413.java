        public boolean postFrame(SessionImpl s, Frame f) throws BEEPException {
            return ((ChannelImpl) f.getChannel()).postFrame(f);
        }
