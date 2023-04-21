        public InputChannel getChannel(int n) {
            if (n != 0) {
                throw new RuntimeException("Getting channel >0 from SingleInputPort.");
            }
            return this;
        }
