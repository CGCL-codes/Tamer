        protected SelectableChannel getChannel() {
            synchronized (sync) {
                return sch;
            }
        }
