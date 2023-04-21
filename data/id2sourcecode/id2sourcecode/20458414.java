        public OutputChannel getChannel(int n) {
            if (n != 0) {
                throw new RuntimeException("Getting channel >0 from SingleOutputPort.");
            }
            return this;
        }
