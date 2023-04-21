        protected SelectableChannel getChannel() {
            synchronized (sync) {
                return dch;
            }
        }
