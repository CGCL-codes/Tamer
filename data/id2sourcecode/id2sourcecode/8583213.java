        public void answer() {
            observer.onAnswer();
            try {
                getChannelApplet().run(new MockRequest(), new MockChannel(getAddress()));
            } catch (AgiException e) {
                LOG.debug("exception while running mock channel applet", e);
            }
        }
